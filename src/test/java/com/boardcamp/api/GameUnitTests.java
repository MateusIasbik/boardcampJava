package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.exceptions.ConflictException;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.repositories.GameStockRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.services.GamesService;

@SpringBootTest
class GameUnitTests {

	@InjectMocks
	private GamesService gamesService;
	
	@Mock
	private GamesRepository gamesRepository;

	@Mock
	private GameStockRepository gameStockRepository;


	@Test
	void givenRepeatedName_whenCreatingGame_thenThrowsError() {
		
		GamesDTO game = new GamesDTO("test", "test", 2, 2000);
		GamesModel gameCreated = new GamesModel(1L, "test", "test", 2, 2000);
		doReturn(Optional.of(gameCreated)).when(gamesRepository).findByName(any());

		ConflictException exception = assertThrows(
			ConflictException.class, 
			() -> gamesService.createGame(game));

		verify(gamesRepository, times(1)).findByName(any());
		verify(gamesRepository, times(0)).save(any());
		assertNotNull(exception);
		assertEquals("O nome já existe. Tente outro.", exception.getMessage());
	}

	@Test
	void givenUniqueGame_whenCreatingGame_thenCreateGameSuccessfully() {
		
		GamesDTO game = new GamesDTO("test", "test", 1, 1000);
		GamesModel gameCreated = new GamesModel(1L, "test", "test", 1, 1000);
		doReturn(Optional.empty()).when(gamesRepository).findByName(any());
		doReturn(gameCreated).when(gamesRepository).save(any());

		GamesModel createdGame = gamesService.createGame(game);

		verify(gamesRepository, times(1)).findByName(any());
		verify(gamesRepository, times(1)).save(any());
		verify(gamesRepository, times(1)).save(any());
		
		assertNotNull(createdGame);
		assertEquals("test", createdGame.getName());
		assertEquals("test", createdGame.getImage());
		assertEquals(1, createdGame.getStockTotal());
		assertEquals(1000, createdGame.getPricePerDay());
	}

	@Test
	void givenNewGame_whenCreatingGame_thenGameStockisCreated() {

		GamesDTO game = new GamesDTO("test", "test", 2, 1000);
		GamesModel gameCreated = new GamesModel(1L, "test", "test", 1, 1000);
		doReturn(Optional.empty()).when(gamesRepository).findByName(any());
		doReturn(gameCreated).when(gamesRepository).save(any());

		gamesService.createGame(game);

		verify(gameStockRepository, times(1)).save(any());
	}

}
