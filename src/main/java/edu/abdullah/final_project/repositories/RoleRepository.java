package edu.abdullah.final_project.repositories;

import edu.abdullah.final_project.entities.Role;
import edu.abdullah.final_project.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName role);


}
