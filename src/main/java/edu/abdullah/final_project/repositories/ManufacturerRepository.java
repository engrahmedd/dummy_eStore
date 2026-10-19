package edu.abdullah.final_project.repositories;

import edu.abdullah.final_project.entities.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    Optional<Manufacturer> findByName(String name);

}