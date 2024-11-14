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

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GameStockRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GameIntegrationTests {

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

    @Test
    void givenValidGame_whenCreatingGame_thenCreateGameSuccessfully() {

        GamesDTO gameDTO = new GamesDTO(
                "test",
                "test",
                2,
                2000);

        HttpEntity<GamesDTO> body = new HttpEntity<>(gameDTO);

        ResponseEntity<GamesDTO> response = restTemplate.exchange(
                "/games",
                HttpMethod.POST,
                body,
                GamesDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        GamesDTO createdGameDTO = response.getBody();

        assertEquals(gameDTO.getName(), createdGameDTO.getName());
        assertEquals(1, gamesRepository.count());
    }

    @Test
    void givenRepeatedGame_whenCreatingGame_thenThrowsError() {

        GamesModel game = new GamesModel(null, "test", "test", 2, 2000);
        gamesRepository.save(game);

        GamesDTO gameDTO = new GamesDTO(
                game.getName(),
                "test",
                2,
                2000);

        HttpEntity<GamesDTO> body = new HttpEntity<>(gameDTO);

        ResponseEntity<String> response = restTemplate.exchange(
                "/games",
                HttpMethod.POST,
                body,
                String.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("O nome já existe. Tente outro.", response.getBody());
        assertEquals(0, rentalsRepository.count());
    }

    @Test
    void givenNewGame_whenCreatingGame_thenGameStockisCreated() {

        GamesDTO gameDTO = new GamesDTO(
                "test",
                "test",
                2,
                2000);

        HttpEntity<GamesDTO> body = new HttpEntity<>(gameDTO);

        ResponseEntity<GamesModel> response = restTemplate.exchange(
                "/games",
                HttpMethod.POST,
                body,
                GamesModel.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, gameStockRepository.count());

    }

}
