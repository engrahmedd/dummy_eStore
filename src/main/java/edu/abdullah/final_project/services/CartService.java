package edu.abdullah.final_project.services;

import edu.abdullah.final_project.entities.Cart;
import edu.abdullah.final_project.entities.User;
import java.math.BigDecimal;

public interface CartService {
    Cart getOrCreateCart(User user);
    void addToCart(String productCode, int quantity);
    void updateQuantity(Long cartItemId, int quantity);
    void removeItem(Long cartItemId);
    void clearCart(User user);
    Cart getCurrentUserCart();
    BigDecimal getCartTotal(Cart cart);
}