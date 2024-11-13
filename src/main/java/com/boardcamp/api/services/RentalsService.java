package com.boardcamp.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.exceptions.NotfoundException;
import com.boardcamp.api.exceptions.UnprocessableEntityException;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GameStockModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.models.RentalsModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GameStockRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

@Service
public class RentalsService {

    private final RentalsRepository rentalsRepository;
    private final GamesRepository gamesRepository;
    private final CustomersRepository customersRepository;
    private final GameStockRepository gameStockRepository;

    RentalsService(RentalsRepository rentalsRepository, GamesRepository gamesRepository,
            CustomersRepository customersRepository, GameStockRepository gameStockRepository) {
        this.rentalsRepository = rentalsRepository;
        this.gamesRepository = gamesRepository;
        this.customersRepository = customersRepository;
        this.gameStockRepository = gameStockRepository;
    }

    public List<RentalsModel> getAllRentals() {
        return rentalsRepository.findAll();
    }

    public RentalsModel createRental(RentalsDTO body) {
        GamesModel game = gamesRepository.findById(body.getGameId())
                .orElseThrow(() -> new NotfoundException("O ID " + body.getGameId() + " do jogo não foi encontrado."));

        CustomersModel customer = customersRepository.findById(body.getCustomerId())
                .orElseThrow(() -> new NotfoundException(
                        "O ID " + body.getCustomerId() + " do cliente não foi encontrado."));

        GameStockModel gameStock = gameStockRepository.findByGameId(body.getGameId());

        if(gameStock.getAvailableStock() == 0) {
            throw new UnprocessableEntityException("Não há jogos disponíveis em estoque.");
        }

        RentalsModel rentals = new RentalsModel(body, game, customer);
        rentals.setOriginalPrice(game.getPricePerDay() * body.getDaysRented());
        gameStock.setAvailableStock(gameStock.getAvailableStock() - 1);
        return rentalsRepository.save(rentals);
    }

    public void deleteRental(Long id) {
        rentalsRepository.deleteById(id);
    }

}
