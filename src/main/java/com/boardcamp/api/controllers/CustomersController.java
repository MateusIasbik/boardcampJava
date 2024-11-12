package com.boardcamp.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.services.CustomersService;

@RestController
@RequestMapping("/customers")
public class CustomersController {

    private final CustomersService customersService;

    CustomersController(CustomersService customersService) {
        this.customersService = customersService;
    }

    @GetMapping()
    public ResponseEntity<List<CustomersModel>> getAllCustomers() {
        List<CustomersModel> customers = customersService.getAllCustomers();
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    
}
