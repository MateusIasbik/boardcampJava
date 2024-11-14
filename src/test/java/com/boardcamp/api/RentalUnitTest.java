package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.exceptions.ConflictException;
import com.boardcamp.api.exceptions.NotfoundException;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GameStockModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.models.RentalsModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GameStockRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;
import com.boardcamp.api.services.RentalsService;

@SpringBootTest
class RentalUnitTest {

    @InjectMocks
    private RentalsService rentalsService;

    @Mock
    private RentalsRepository rentalsRepository;

    @Mock
    private GamesRepository gamesRepository;

    @Mock
    private CustomersRepository customersRepository;

    @Mock
    private GameStockRepository gameStockRepository;

    @Test
    void givenValidRental_whenCreatingRental_thenCreatesRental() {

        RentalsDTO rentalsDTO = new RentalsDTO(1L, 1L, 2);

        Long validGameId = 1L;
        Long validCustomerId = 1L;
        int availableStock = 3;

        GamesModel game = new GamesModel(validGameId, "test", "test", availableStock, 2000);

        CustomersModel customer = new CustomersModel(validCustomerId, "test", "1111111111", "00000000000");

        GameStockModel gameStock = new GameStockModel(game);

        doReturn(Optional.of(game)).when(gamesRepository).findById(validGameId);
        doReturn(Optional.of(customer)).when(customersRepository).findById(validCustomerId);
        doReturn(gameStock).when(gameStockRepository).findByGameId(validGameId);

        RentalsModel savedRental = new RentalsModel(rentalsDTO, game, customer);
        savedRental.setOriginalPrice(game.getPricePerDay() * rentalsDTO.getDaysRented());
        doReturn(savedRental).when(rentalsRepository).save(any());

        RentalsModel createdRental = rentalsService.createRental(rentalsDTO);

        assertNotNull(createdRental);
        assertEquals(game.getPricePerDay() * rentalsDTO.getDaysRented(), createdRental.getOriginalPrice());
        assertEquals(validGameId, createdRental.getGame().getId());
        assertEquals(validCustomerId, createdRental.getCustomer().getId());

        assertEquals(availableStock - 1, gameStock.getAvailableStock());

        verify(gamesRepository).findById(validGameId);
        verify(customersRepository).findById(validCustomerId);
        verify(gameStockRepository).findByGameId(validGameId);
        verify(gameStockRepository).save(gameStock);
        verify(rentalsRepository).save(createdRental);
    }

    @Test
    void givenGameIdNotfound_whenCreatingRental_thenThrowsError() {

        RentalsDTO rentalsDTO = new RentalsDTO(1L, 1L, 2);

        doReturn(Optional.empty()).when(gamesRepository).findById(1L);

        NotfoundException exception = assertThrows(
            NotfoundException.class,
            () -> rentalsService.createRental(rentalsDTO));

        assertNotNull(exception);
        assertEquals("O ID " + 1L + " do jogo não foi encontrado.", exception.getMessage());

    }

    @Test
    void givenCustomerIdNotfound_whenCreatingRental_thenThrowsError() {
        
        RentalsDTO rentalsDTO = new RentalsDTO(1L, 1L, 2);

        GamesModel game = new GamesModel(1L, "test", "test", 2, 2000);

        doReturn(Optional.of(game)).when(gamesRepository).findById(1L);
        
        doReturn(Optional.empty()).when(customersRepository).findById(1L);

        NotfoundException exception = assertThrows(
            NotfoundException.class,
            () -> rentalsService.createRental(rentalsDTO));

        assertNotNull(exception);
        assertEquals("O ID " + 1L + " do cliente não foi encontrado.", exception.getMessage());
    }

}
