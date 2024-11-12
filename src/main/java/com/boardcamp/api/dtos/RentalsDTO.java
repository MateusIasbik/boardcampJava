package com.boardcamp.api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalsDTO {

    @NotNull
    private Long customerId;

    @NotNull
    private Long gameId;

    @NotNull
    @Positive
    @Min(value = 1)
    private int daysRented;

}
