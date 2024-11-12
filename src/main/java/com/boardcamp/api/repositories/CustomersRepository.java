package com.boardcamp.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boardcamp.api.models.CustomersModel;

public interface CustomersRepository extends JpaRepository<CustomersModel, Long> {
    
}
