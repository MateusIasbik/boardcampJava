package com.boardcamp.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.exceptions.ConflictException;
import com.boardcamp.api.models.GameStockModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.repositories.GameStockRepository;
import com.boardcamp.api.repositories.GamesRepository;

@Service
public class GamesService {
    
    private final GamesRepository gamesRepository;
    private final GameStockRepository gameStockRepository;

    GamesService(GamesRepository gamesRepository, GameStockRepository gameStockRepository) {
        this.gamesRepository = gamesRepository;
        this.gameStockRepository = gameStockRepository;
    }

    public List<GamesModel> getAllGames() {
        return gamesRepository.findAll();
    }

    public GamesModel createGame(GamesDTO body) {
        Optional<GamesModel> nameExists = gamesRepository.findByName(body.getName());
        if (nameExists.isPresent()) {
            throw new ConflictException("O nome já existe. Tente outro.");
        }

        GamesModel game = new GamesModel(body);
        GamesModel gameCreated = gamesRepository.save(game);
        GameStockModel gameStock = new GameStockModel(gameCreated);
        gameStockRepository.save(gameStock);
        return gameCreated;
    }

    public void deleteGame(Long id) {
        GameStockModel gameStock = gameStockRepository.findByGameId(id);
        if (gameStock != null) {
            gameStockRepository.delete(gameStock);
        }
        gamesRepository.deleteById(id);
    }

}
