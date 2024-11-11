package com.boardcamp.api.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb-rentals")
public class RentalsModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private CustomersModel customer;
    
    @ManyToOne
    @JoinColumn(name = "gameId", nullable = false)
    private GamesModel game;

    private LocalDateTime rentDate = LocalDateTime.now();

    @Positive
    @Min(value = 1, message = "O número de dias alugados deve ser maior que 0.")
    private int daysRented;

    private Data returnDate = null;

    private int originalPrice;

    private int delayFee = 0;

}
