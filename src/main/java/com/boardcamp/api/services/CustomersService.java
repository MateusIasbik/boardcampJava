package com.boardcamp.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.repositories.CustomersRepository;

@Service
public class CustomersService {
    
    private final CustomersRepository customersRepository;

    CustomersService(CustomersRepository customersRepository) {
        this.customersRepository = customersRepository;
    }

    public List<CustomersModel> getAllCustomers() {
        return customersRepository.findAll();
    }

}
