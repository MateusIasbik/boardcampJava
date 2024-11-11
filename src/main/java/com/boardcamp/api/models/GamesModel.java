package com.boardcamp.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb-games")
public class GamesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 150, nullable = false)
    @NotBlank(message = "O nome não pode ser vazio")
    private String name;

    @Column
    private String image;

    @Column(nullable = false)
    @Positive(message = "O estoque deve ser maior que zero.")
    private int stockTotal;

    @Column(nullable = false)
    @Positive(message = "O preço deve ser maior que zero.")
    private int pricePerDay;

}
