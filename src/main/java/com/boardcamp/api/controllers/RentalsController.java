package com.boardcamp.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.models.RentalsModel;
import com.boardcamp.api.services.RentalsService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/rentals")
public class RentalsController {

    private final RentalsService rentalsService;

    RentalsController(RentalsService rentalsService) {
        this.rentalsService = rentalsService;
    }

    @GetMapping()
    public ResponseEntity<List<RentalsModel>> getAllRentals() {
        List<RentalsModel> rentals = rentalsService.getAllRentals();
        return ResponseEntity.status(HttpStatus.OK).body(rentals);
    }

    @PostMapping()
    public ResponseEntity<RentalsModel> createRental(@RequestBody @Valid RentalsDTO body) {
        RentalsModel rental = rentalsService.createRental(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<RentalsModel> returnRental(@PathVariable("id") Long id) {
        RentalsModel rental = rentalsService.returnRental(id);
        return ResponseEntity.status(HttpStatus.OK).body(rental);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<RentalsModel> deleteRental(@PathVariable("id") Long id) {
        rentalsService.deleteRental(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
