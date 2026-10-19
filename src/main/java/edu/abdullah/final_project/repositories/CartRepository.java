package edu.abdullah.final_project.repositories;

import edu.abdullah.final_project.entities.Cart;
import edu.abdullah.final_project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}