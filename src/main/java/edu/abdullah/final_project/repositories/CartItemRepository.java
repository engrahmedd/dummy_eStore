package edu.abdullah.final_project.repositories;

import edu.abdullah.final_project.entities.Cart;
import edu.abdullah.final_project.entities.CartItem;
import edu.abdullah.final_project.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    boolean existsByProduct(Product product);
}