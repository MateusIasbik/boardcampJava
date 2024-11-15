package com.boardcamp.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boardcamp.api.models.CustomersModel;

public interface CustomersRepository extends JpaRepository<CustomersModel, Long> {
    public Optional<CustomersModel> findByCpf(String cpf);
}
