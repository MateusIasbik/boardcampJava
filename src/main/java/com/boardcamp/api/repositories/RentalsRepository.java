package com.boardcamp.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.models.RentalsModel;

public interface RentalsRepository extends JpaRepository<RentalsModel, Long> {
    
    @Query(nativeQuery = true, value = "SELECT * " +
            "FROM \"tb-rentals\" AS rentals " +
            "INNER JOIN \"tb-customers\" AS customers ON rentals.customer_id = customers.id " +
            "INNER JOIN \"tb-games\" AS games ON rentals.game_id = games.id " +
            "WHERE rentals.customer_id = :customerId AND rentals.game_id = :gamesId;")
    List<GamesModel> getRentalsById(@Param("customerId") Long customerId, @Param("gamesId") Long gamesId);

}
