package edu.abdullah.final_project.repositories;

import edu.abdullah.final_project.entities.Order;
import edu.abdullah.final_project.entities.Product;
import edu.abdullah.final_project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> getOrdersByUser(User user);

    Order getOrderById(Integer orderId);

    List<Order> findByUserOrderByOrderDateDesc(User currentUser);

    Optional<Order> findByOrderNumber(String orderNumber);

    boolean existsByItemsProduct(Product product);
}
