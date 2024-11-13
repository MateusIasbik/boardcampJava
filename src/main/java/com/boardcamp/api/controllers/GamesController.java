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

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.services.GamesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/games")
public class GamesController {
    
    private final GamesService gamesService;

    GamesController(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @GetMapping()
    public ResponseEntity<List<GamesModel>> getAllGames() {
        List<GamesModel> games = gamesService.getAllGames();
        return ResponseEntity.status(HttpStatus.OK).body(games);
    }

    @PostMapping()
    public ResponseEntity<GamesModel> createGame(@RequestBody @Valid GamesDTO body) {
        GamesModel game = gamesService.createGame(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<GamesModel> deleteGame(@PathVariable("id") Long id) {
        gamesService.deleteGame(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
