package com.boardcamp.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.exceptions.CustomerIdNotfoundException;
import com.boardcamp.api.exceptions.ConflictException;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
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

    public CustomersModel getCustomerById(Long id) {
        Optional<CustomersModel> customer = customersRepository.findById(id);

        if(!customer.isPresent()) {
            throw new CustomerIdNotfoundException("O ID do cliente não foi encontrado.");
        }

        return customer.get();
    }

    public CustomersModel createCustomer(CustomersDTO body) {
        Optional<CustomersModel> cpfExists = customersRepository.findByCpf(body.getCpf());
        if (cpfExists.isPresent()) {
            throw new ConflictException("O cpf já existe. Tente outro.");
        }

        CustomersModel customer = new CustomersModel(body);
        return customersRepository.save(customer);
    }

}
