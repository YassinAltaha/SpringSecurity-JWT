package exo.spring.jwtSecurtiy.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import exo.spring.jwtSecurtiy.domain.AppUser;
import exo.spring.jwtSecurtiy.domain.Role;
import exo.spring.jwtSecurtiy.repo.RoleRepo;
import exo.spring.jwtSecurtiy.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService{

	
	private final UserRepo userRepo;
	private final RoleRepo roleRepo;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public AppUser saveUser(AppUser user) {
		log.info("Saving new user to the database "+ user.getUsername());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return userRepo.save(user);
	}

	@Override
	public Role saveRole(Role role) {
		log.info("Saving new Role to the database " + role.getName());
		return roleRepo.save(role);
	}

	@Override
	public void addRoleToUser(String username, String roleName) {
		
		log.info("Add new Role {} to user {} " ,roleName, username);
		AppUser user = userRepo.findByUsername(username);
		Role role = roleRepo.findByName(roleName);
		user.getRoles().add(role);
		
	}

	@Override
	public AppUser getUser(String username) {
		log.info("Fetching user {} " , username);
		return userRepo.findByUsername(username);
	}

	@Override
	public List<AppUser> getUsers() {
		log.info("Fetching ALL users");
		return userRepo.findAll();
		}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		AppUser user = userRepo.findByUsername(username);
		if(user == null) {
			log.error("User Not Found");
			throw new UsernameNotFoundException("User not found in database");
			
		}else {
			log.info("User found in database: {}", username);
			
		}
		
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}

}
