package edu.abdullah.final_project.services;

import edu.abdullah.final_project.dtos.ChangePasswordDTO;
import edu.abdullah.final_project.dtos.EditAccountDTO;
import edu.abdullah.final_project.entities.Order;
import edu.abdullah.final_project.entities.User;

import java.util.List;

public interface UserService {
    User getCurrentUser();
    void updateAccount(EditAccountDTO dto);
    void changePassword(ChangePasswordDTO dto);
    List<Order> getOrderHistory();
    Order getOrderByNumber(String orderNumber);
}
