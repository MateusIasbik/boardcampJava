package com.boardcamp.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamesDTO {
    
    @NotBlank
    private String name;

    private String image;

    @NotNull
    @Positive
    private int stockTotal;

    @NotNull
    @Positive
    private int pricePerDay;

    
}
