package edu.abdullah.final_project.services;

import edu.abdullah.final_project.dtos.CheckoutDTO;
import edu.abdullah.final_project.entities.Cart;
import edu.abdullah.final_project.entities.Order;
import edu.abdullah.final_project.entities.OrderItem;
import edu.abdullah.final_project.entities.User;
import edu.abdullah.final_project.enums.OrderStatus;
import edu.abdullah.final_project.exceptions.EmptyCartException;
import edu.abdullah.final_project.exceptions.PaymentDeclinedException;
import edu.abdullah.final_project.repositories.OrderRepository;
import edu.abdullah.final_project.repositories.UserRepository;
import edu.abdullah.final_project.utils.PaymentProcessor;
import edu.abdullah.final_project.utils.PaymentRequest;
import edu.abdullah.final_project.utils.PaymentResult;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentProcessor paymentProcessor;

    public CheckoutServiceImpl(CartService cartService,
                               OrderRepository orderRepository,
                               UserRepository userRepository,
                               PaymentProcessor paymentProcessor) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.paymentProcessor = paymentProcessor;
    }

    @Override
    @Transactional
    public Order checkout(CheckoutDTO dto) {
        User user = getCurrentUser();
        Cart cart = cartService.getCurrentUserCart();

        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException("Cannot checkout with an empty cart.");
        }

        BigDecimal total = cartService.getCartTotal(cart);

        // Process payment
        PaymentRequest paymentRequest = new PaymentRequest(
                dto.getCardholderName(),
                dto.getCardNumber(),
                dto.getExpirationMonth(),
                dto.getExpirationYear(),
                dto.getCvv(),
                total
        );

        PaymentResult result = paymentProcessor.processPayment(paymentRequest);

        if (!result.isApproved()) {
            throw new PaymentDeclinedException(result.getMessage());
        }

        // Build order
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PAID);
        order.setTransactionId(result.getTransactionId());
        order.setShippingName(dto.getShippingName());
        order.setShippingAddress(dto.getShippingAddress());
        order.setShippingCity(dto.getShippingCity());
        order.setShippingState(dto.getShippingState());
        order.setShippingZip(dto.getShippingZip());
        order.setOrderTotal(total);

        // Build order items from cart
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity()))
                .toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Clear cart after successful order
        cartService.clearCart(user);

        return savedOrder;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EmptyCartException("Cannot checkout with an empty cart."));
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}