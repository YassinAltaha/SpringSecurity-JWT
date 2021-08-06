package exo.spring.jwtSecurtiy.api;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import exo.spring.jwtSecurtiy.domain.AppUser;
import exo.spring.jwtSecurtiy.domain.Role;
import exo.spring.jwtSecurtiy.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
	private final UserService userService;
	
	
	
	@GetMapping("/all-users")
	public ResponseEntity <List<AppUser>>getUsers(){
		
		return ResponseEntity.ok().body(userService.getUsers());
		}
	
	@PostMapping("/user/save")
	public ResponseEntity <AppUser>saveUsers(@RequestBody AppUser user){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
		return ResponseEntity.created(uri).body(userService.saveUser(user));
		}
	
	@PostMapping("/role/save")
	public ResponseEntity <Role>saveRole(@RequestBody Role role){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
		return ResponseEntity.created(uri).body(userService.saveRole(role));
		}
	
	@PostMapping("/role/addToUser")
	public ResponseEntity <?>addRoleToUser(@RequestBody RoleToUserForm form){
		userService.addRoleToUser(form.getUsername(), form.getRoleName());
		return ResponseEntity.ok().build();
		}
	
	@GetMapping("/token/refreshToken")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response){
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			
			try {
				String refresh_token = authorizationHeader.substring("Bearer ".length());
				
				Algorithm alogorithem = Algorithm.HMAC256("secret".getBytes());
				
				JWTVerifier verifier = JWT.require(alogorithem).build();
				
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				
				String username = decodedJWT.getSubject();
				
				AppUser user = userService.getUser(username);
				
				String access_token = JWT.create()
						.withSubject(user.getUsername())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", user.getRoles().stream().map(Role::getName)
								.collect(Collectors.toList()))
						.sign(alogorithem);

				

				Map<String, String> tokens= new HashMap<>();

				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);

				response.setContentType(APPLICATION_JSON_VALUE);

				new ObjectMapper().writeValue(response.getOutputStream(), tokens);

				
				
				
				
				
			}catch(Exception e) {
				
				
				response.setHeader("Error", e.getMessage());
				//response.sendError(FORBIDDEN.value());
				response.setStatus(FORBIDDEN.value());
				Map<String, String> error= new HashMap<>();
				
				error.put("error_token", e.getMessage());
				
				response.setContentType(APPLICATION_JSON_VALUE);
				
				//new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
			
			
		}else {
			throw new RuntimeException("Refresh token not found" );
		}
		
		}
	

}

@Data
class RoleToUserForm{
	private String username;
	private String roleName;
}
