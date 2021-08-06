package exo.spring.jwtSecurtiy.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import exo.spring.jwtSecurtiy.domain.AppUser;

public interface UserRepo extends JpaRepository<AppUser,Long>{
	AppUser findByUsername(String username);
	
	
}
