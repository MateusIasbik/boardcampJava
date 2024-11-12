package com.boardcamp.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.exceptions.ConflictException;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.repositories.GamesRepository;

@Service
public class GamesService {
    
    private final GamesRepository gamesRepository;

    GamesService(GamesRepository gamesRepository) {
        this.gamesRepository = gamesRepository;
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
        return gamesRepository.save(game);
    }

}
