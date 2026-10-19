package edu.abdullah.final_project.services;

import edu.abdullah.final_project.dtos.RegistrationDTO;
import edu.abdullah.final_project.entities.User;
import edu.abdullah.final_project.enums.RoleName;
import edu.abdullah.final_project.repositories.RoleRepository;
import edu.abdullah.final_project.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public boolean register(RegistrationDTO dto, HttpSession session) {

        if (!dto.passwordsMatch()) {
            flashError(session, "Passwords do not match.");
            return false;
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            flashError(session, "An account with that email already exists.");
            return false;
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Set.of(roleRepository.findByName(RoleName.CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Role not found"))));

        userRepository.save(user);
        flashSuccess(session, "Registration successful! Please sign in.");
        return true;
    }

    public void flashError(HttpSession session, String message) {
        session.setAttribute("flashError", message);
    }

    public void flashSuccess(HttpSession session, String message) {
        session.setAttribute("flashSuccess", message);
    }

    public String consumeError(HttpSession session) {
        String msg = (String) session.getAttribute("flashError");
        session.removeAttribute("flashError");
        return msg;
    }

    public String consumeSuccess(HttpSession session) {
        String msg = (String) session.getAttribute("flashSuccess");
        session.removeAttribute("flashSuccess");
        return msg;
    }
}