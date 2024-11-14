package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GameStockModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.models.RentalsModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GameStockRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RentalIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GamesRepository gamesRepository;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private RentalsRepository rentalsRepository;

    @Autowired
    private GameStockRepository gameStockRepository;

    @BeforeEach
    @AfterEach
    void cleanUpDatabase() {
        rentalsRepository.deleteAll();
        gameStockRepository.deleteAll();
        gamesRepository.deleteAll();
        customersRepository.deleteAll();
    }

    @Test
    void givenValidRental_whenCreatingRental_thenCreatesRental() {

        GamesModel game = new GamesModel(null, "test", "test", 2, 2000);
        GamesModel createdGame = gamesRepository.save(game);

        CustomersModel customer = new CustomersModel(null, "test", "1111111111", "00000000000");
        CustomersModel createdCustomer = customersRepository.save(customer);

        gameStockRepository.save(new GameStockModel(createdGame));

        RentalsDTO rentalDTO = new RentalsDTO(
                createdCustomer.getId(),
                createdGame.getId(),
                2);

        HttpEntity<RentalsDTO> body = new HttpEntity<>(rentalDTO);

        ResponseEntity<RentalsModel> response = restTemplate.exchange(
                "/rentals",
                HttpMethod.POST,
                body,
                RentalsModel.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, rentalsRepository.count());

    }

    @Test
    void givenGameIdNotfound_whenCreatingRental_thenThrowsError() {

        GamesModel game = new GamesModel(null, "test", "test", 2, 2000);
        GamesModel createdGame = gamesRepository.save(game);
        gamesRepository.deleteById(createdGame.getId());
        
        CustomersModel customer = new CustomersModel(null, "test", "1111111111", "00000000000");
        CustomersModel createdCustomer = customersRepository.save(customer);
        

        RentalsDTO rentalDTO = new RentalsDTO(
                createdCustomer.getId(),
                createdGame.getId(),
                2);

        HttpEntity<RentalsDTO> body = new HttpEntity<>(rentalDTO);

        ResponseEntity<String> response = restTemplate.exchange(
                "/rentals",
                HttpMethod.POST,
                body,
                String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("O ID " + createdGame.getId() + " do jogo não foi encontrado.", response.getBody());
        assertEquals(0, rentalsRepository.count());

    }


    @Test
    void givenCustomerIdNotfound_whenCreatingRental_thenThrowsError() {

        GamesModel game = new GamesModel(null, "test", "test", 2, 2000);
        GamesModel createdGame = gamesRepository.save(game);
        
        CustomersModel customer = new CustomersModel(null, "test", "1111111111", "00000000000");
        CustomersModel createdCustomer = customersRepository.save(customer);
        customersRepository.deleteById(createdCustomer.getId());
        

        RentalsDTO rentalDTO = new RentalsDTO(
                createdCustomer.getId(),
                createdGame.getId(),
                2);

        HttpEntity<RentalsDTO> body = new HttpEntity<>(rentalDTO);

        ResponseEntity<String> response = restTemplate.exchange(
                "/rentals",
                HttpMethod.POST,
                body,
                String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("O ID " + createdCustomer.getId() + " do cliente não foi encontrado.", response.getBody());
        assertEquals(0, rentalsRepository.count());

    }


    
}