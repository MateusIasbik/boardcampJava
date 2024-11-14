package com.boardcamp.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GameStockRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomerIntegrationTests {

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
        gameStockRepository.deleteAll();
        rentalsRepository.deleteAll();
        customersRepository.deleteAll();
        gamesRepository.deleteAll();
    }

}
