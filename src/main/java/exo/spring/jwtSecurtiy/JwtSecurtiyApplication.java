package exo.spring.jwtSecurtiy;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import exo.spring.jwtSecurtiy.domain.AppUser;
import exo.spring.jwtSecurtiy.domain.Role;
import exo.spring.jwtSecurtiy.service.UserService;

@SpringBootApplication
public class JwtSecurtiyApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtSecurtiyApplication.class, args);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new Role(null , "ROLE_USER"));
			userService.saveRole(new Role(null , "ROLE_MANAGER"));
			userService.saveRole(new Role(null , "ROLE_ADMIN"));
			userService.saveRole(new Role(null , "ROLE_SUPER_ADMIN"));
			
			
			userService.saveUser(new AppUser(null, "John Doe","johndoe","1234", new ArrayList<>()));
			userService.saveUser(new AppUser(null, "Will Smith","willsmith","1234", new ArrayList<>()));
			userService.saveUser(new AppUser(null, "Janet Rose","janetrose","1234", new ArrayList<>()));
			userService.saveUser(new AppUser(null, "Boogie Man","boogieman","1234", new ArrayList<>()));
			
			
			userService.addRoleToUser("johndoe", "ROLE_USER");
			userService.addRoleToUser("janetrose", "ROLE_USER");
			userService.addRoleToUser("janetrose", "ROLE_MANAGER");
			userService.addRoleToUser("willsmith", "ROLE_ADMIN");
			userService.addRoleToUser("boogieman", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("boogieman", "ROLE_ADMIN");
			userService.addRoleToUser("boogieman", "ROLE_USER");
			
		};
		
	}
}
