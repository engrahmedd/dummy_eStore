package edu.abdullah.final_project.services;

import edu.abdullah.final_project.dtos.ChangePasswordDTO;
import edu.abdullah.final_project.dtos.EditAccountDTO;
import edu.abdullah.final_project.entities.Order;
import edu.abdullah.final_project.entities.User;
import edu.abdullah.final_project.repositories.OrderRepository;
import edu.abdullah.final_project.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           OrderRepository orderRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found."));
    }

    @Override
    public void updateAccount(EditAccountDTO dto) {
        User user = getCurrentUser();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordDTO dto) {
        User user = getCurrentUser();

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        if (!dto.passwordsMatch()) {
            throw new IllegalArgumentException("New passwords do not match.");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public List<Order> getOrderHistory() {
        return orderRepository.findByUserOrderByOrderDateDesc(getCurrentUser());
    }

    @Override
    public Order getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Order not found."));

        if (!order.getUser().getId().equals(getCurrentUser().getId())) {
            throw new IllegalStateException("Unauthorized access to order.");
        }

        return order;
    }
}