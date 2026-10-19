package edu.abdullah.final_project.services;

import edu.abdullah.final_project.dtos.RegistrationDTO;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    boolean register(RegistrationDTO dto, HttpSession session);

    void flashError(HttpSession session, String message);

    void flashSuccess(HttpSession session, String message);

    String consumeError(HttpSession session);

    String consumeSuccess(HttpSession session);
}
