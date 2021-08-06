package exo.spring.jwtSecurtiy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import exo.spring.jwtSecurtiy.domain.AppUser;
import exo.spring.jwtSecurtiy.domain.Role;

@Service
public interface UserService {
	
	AppUser saveUser(AppUser user);
	Role saveRole(Role role);
	
	void addRoleToUser(String username, String roleName);
	AppUser getUser(String username);
	
	List<AppUser> getUsers();
}
