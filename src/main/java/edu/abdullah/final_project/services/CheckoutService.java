package edu.abdullah.final_project.services;

import edu.abdullah.final_project.dtos.CheckoutDTO;
import edu.abdullah.final_project.entities.Order;

public interface CheckoutService {
    Order checkout(CheckoutDTO dto);
}