package com.boardcamp.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boardcamp.api.models.GameStockModel;

@Repository
public interface GameStockRepository extends JpaRepository<GameStockModel, Long> {
    public GameStockModel findByGameId(Long gameId);
}