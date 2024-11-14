package com.boardcamp.api.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

        if (gameStock.getAvailableStock() == 0) {
            throw new UnprocessableEntityException("Não há jogos disponíveis em estoque.");
        }

        RentalsModel rentals = new RentalsModel(body, game, customer);
        rentals.setOriginalPrice(game.getPricePerDay() * body.getDaysRented());
        gameStock.setAvailableStock(gameStock.getAvailableStock() - 1);
        gameStockRepository.save(gameStock);
        return rentalsRepository.save(rentals);
    }

    public RentalsModel returnRental(Long id) {
        RentalsModel rental = rentalsRepository.findById(id)
                .orElseThrow(() -> new NotfoundException("O Id " + id + " não foi encontrado."));

        if(rental.getReturnDate() != null) {
            throw new UnprocessableEntityException("Este aluguel já foi finalizado.");
        }

        LocalDate today = LocalDate.now();
        rental.setReturnDate(today);

        if(rental.getRentDate() == null) {
            throw new UnprocessableEntityException("A data de aluguel não pode ser nula.");
        }

        Long daysRented = ChronoUnit.DAYS.between(rental.getRentDate(), rental.getReturnDate());
        Long totalDaysRented = daysRented - rental.getDaysRented();

        if(totalDaysRented > 0) {
            rental.setDelayFee(totalDaysRented * rental.getGame().getPricePerDay());
        }

        GameStockModel gameStock = gameStockRepository.findByGameId(rental.getGame().getId());

        if(gameStock != null) {
            gameStock.setAvailableStock(gameStock.getAvailableStock() + 1);
            gameStockRepository.save(gameStock);
        } else {
            throw new NotfoundException("Estoque não encontrado.");
        }       

        return rentalsRepository.save(rental);
    }

    public void deleteRental(Long id) {
        rentalsRepository.findById(id)
                .orElseThrow(() -> new NotfoundException("O Id " + id + " não foi encontrado."));
        rentalsRepository.deleteById(id);
    }

}
