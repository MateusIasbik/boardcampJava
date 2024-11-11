package com.boardcamp.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb-customers")
public class CustomersModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "O nome não pode ser vazio.")
    private String name;

    @Size(min = 10, max = 11, message = "O número deve ter entre 10 a 11 caracteres.")
    private String phone;

    @Column(nullable = false)
    @NotBlank(message = "O nome não pode ser vazio.")
    @Size(min = 11, max = 11, message = "O número deve ter exatamente 11 caracteres.")
    private String cpf;
}
