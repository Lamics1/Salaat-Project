package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.Model.*;
import com.finalproject.tuwaiqfinal.Repository.GameRepository;
import com.finalproject.tuwaiqfinal.Repository.HallRepository;
import com.finalproject.tuwaiqfinal.Repository.OwnerRepository;
import com.finalproject.tuwaiqfinal.Repository.SubHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final OwnerRepository ownerRepository;
    private final HallRepository hallRepository;
    private final SubHallRepository subHallRepository;

    public List<Game> getAllGame() {
        return gameRepository.findAll();
    }

    public void AddGame(Integer owner_id, Integer hall_id, Integer subHall_id, Game game) {

        Owner owner = ownerRepository.findOwnerById(owner_id);
        if (owner == null) {
            throw new ApiException("Owner not found");
        }
        Hall hall = hallRepository.findHallById(hall_id);
        if (hall == null) {
            throw new ApiException("Hall not found");
        }
        SubHall subHall = subHallRepository.findSubHallsById(subHall_id);
        if (subHall == null) {
            throw new ApiException("subHall not found");
        }
        game.setSubHall(subHall);
        gameRepository.save(game);
    }
  public void UpdateGame(Integer owner_id, Integer hall_id, Integer subHall_id, Integer game_id , Game game ){

      Owner owner = ownerRepository.findOwnerById(owner_id);
      if (owner == null) {
          throw new ApiException("Owner not found");
      }
      Hall hall = hallRepository.findHallById(hall_id);
      if (hall == null) {
          throw new ApiException("Hall not found");
      }
      SubHall subHall = subHallRepository.findSubHallsById(subHall_id);
      if (subHall == null) {
          throw new ApiException("subHall not found");
      }
      Game oldGame = gameRepository.findGameById(game_id);
      if (oldGame == null){
          throw new ApiException("Game not found");
      }

      game.setSubHall(subHall);

      oldGame.setColor(game.getColor());
      oldGame.setIsAvailable(game.getIsAvailable());
      oldGame.setNumberOfPlayer(game.getNumberOfPlayer());

      gameRepository.save(oldGame);
    }
    public void DeleteGame(Integer owner_id, Integer hall_id, Integer subHall_id, Integer game_id){
        Owner owner = ownerRepository.findOwnerById(owner_id);
        if (owner == null) {
            throw new ApiException("Owner not found");
        }
        Hall hall = hallRepository.findHallById(hall_id);
        if (hall == null) {
            throw new ApiException("Hall not found");
        }
        SubHall subHall = subHallRepository.findSubHallsById(subHall_id);
        if (subHall == null) {
            throw new ApiException("subHall not found");
        }

        Game game = gameRepository.findGameById(game_id);
        if (game == null){
            throw new ApiException("Game not found");
        }
        gameRepository.delete(game);
    }
}