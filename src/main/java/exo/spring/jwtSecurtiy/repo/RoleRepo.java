package exo.spring.jwtSecurtiy.repo;

import org.springframework.data.jpa.repository.JpaRepository;


import exo.spring.jwtSecurtiy.domain.Role;

public interface RoleRepo extends JpaRepository<Role,Long>{
	Role findByName(String name);
	
	
}