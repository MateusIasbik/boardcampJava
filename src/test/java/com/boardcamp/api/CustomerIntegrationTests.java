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

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.models.CustomersModel;
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

    @Test
    void givenValidCustomer_whenCreatingCustomer_thenCreateGameSuccessfully() {

        CustomersDTO customersDTO = new CustomersDTO(
                "test",
                "1111111111",
                "00000000000");

        HttpEntity<CustomersDTO> body = new HttpEntity<>(customersDTO);

        ResponseEntity<CustomersDTO> response = restTemplate.exchange(
                "/customers",
                HttpMethod.POST,
                body,
                CustomersDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        CustomersDTO createdCustomerDTO = response.getBody();

        assertEquals(customersDTO.getName(), createdCustomerDTO.getName());
        assertEquals(1, customersRepository.count());
    }

    @Test
    void givenRepeatedCpf_whenCreatingCustomer_thenThrowsError() {

        CustomersModel customer = new CustomersModel(null, "test", "0000000000", "11111111111");
        customersRepository.save(customer);

        CustomersDTO customerDTO = new CustomersDTO(
                customer.getName(),
                customer.getPhone(),
                customer.getCpf());

        HttpEntity<CustomersDTO> body = new HttpEntity<>(customerDTO);

        ResponseEntity<String> response = restTemplate.exchange(
                "/customers",
                HttpMethod.POST,
                body,
                String.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("O cpf já existe. Tente outro.", response.getBody());
        assertEquals(1, customersRepository.count());
    }

    @Test
    void givenCpfWithDifferentData_whenCreatingCustomer_thenThrowsError() {

        CustomersModel customer = new CustomersModel(null, "test", "0000000000", "11111111111");
        customersRepository.save(customer);

        CustomersDTO customerDTO = new CustomersDTO(
                "nomeDiferente",
                "9999999999",
                customer.getCpf());

        HttpEntity<CustomersDTO> body = new HttpEntity<>(customerDTO);

        ResponseEntity<String> response = restTemplate.exchange(
                "/customers",
                HttpMethod.POST,
                body,
                String.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("O cpf já existe. Tente outro.", response.getBody());
        assertEquals(1, customersRepository.count());

    }

}
