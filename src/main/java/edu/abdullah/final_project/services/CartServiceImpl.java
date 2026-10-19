package edu.abdullah.final_project.services;

import edu.abdullah.final_project.entities.Cart;
import edu.abdullah.final_project.entities.CartItem;
import edu.abdullah.final_project.entities.Product;
import edu.abdullah.final_project.entities.User;
import edu.abdullah.final_project.exceptions.CartOwnershipException;
import edu.abdullah.final_project.exceptions.ProductNotFoundException;
import edu.abdullah.final_project.repositories.CartItemRepository;
import edu.abdullah.final_project.repositories.CartRepository;
import edu.abdullah.final_project.repositories.ProductRepository;
import edu.abdullah.final_project.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    @Override
    public void addToCart(String productCode, int quantity) {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findByProductCode(productCode)
                .filter(Product::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Product not found or inactive."));

        cartItemRepository.findByCartAndProduct(cart, product)
                .ifPresentOrElse(
                        item -> {
                            item.setQuantity(item.getQuantity() + quantity);
                            cartItemRepository.save(item);
                        },
                        () -> cartItemRepository.save(new CartItem(cart, product, quantity))
                );
    }

    @Override
    public void updateQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found or inactive."));

        validateCartOwnership(item);

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    @Override
    public void removeItem(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ProductNotFoundException("Cart item not found."));

        validateCartOwnership(item);

        Cart cart = item.getCart();
        cart.getItems().remove(item);
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(User user) {
        cartRepository.findByUser(user).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepository.save(cart);
        });
    }

    @Override
    public Cart getCurrentUserCart() {
        return getOrCreateCart(getCurrentUser());
    }

    @Override
    public BigDecimal getCartTotal(Cart cart) {
        return cart.getItems().stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ── Private helpers ──

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() ->  new CartOwnershipException("Unauthorized cart access."));
    }

    private void validateCartOwnership(CartItem item) {
        User currentUser = getCurrentUser();
        if (!item.getCart().getUser().getId().equals(currentUser.getId())) {
            throw new CartOwnershipException("Unauthorized cart access.");
        }
    }
}
