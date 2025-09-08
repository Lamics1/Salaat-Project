package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.DTOout.GameDTO;
import com.finalproject.tuwaiqfinal.Model.*;
import com.finalproject.tuwaiqfinal.Repository.GameRepository;
import com.finalproject.tuwaiqfinal.Repository.HallRepository;
import com.finalproject.tuwaiqfinal.Repository.OwnerRepository;
import com.finalproject.tuwaiqfinal.Repository.SubHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

//    repos DI
    private final GameRepository gameRepository;
    private final OwnerRepository ownerRepository;
    private final HallRepository hallRepository;
    private final SubHallRepository subHallRepository;

    public List<GameDTO> getAllGame() {
        return gameRepository.findAll()
                .stream()
                .map(game -> {
                    GameDTO dto = new GameDTO();
                    dto.setId(game.getId());
                    dto.setColor(game.getColor());

                    dto.setNumberOfPlayer(game.getNumberOfPlayer());
                    return dto;
                })
                .collect(Collectors.toList());
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
        game.setSubHall(subHall); // todo: link game with a booking (take booking id from parameter)
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

      oldGame.setSubHall(subHall);

      oldGame.setColor(game.getColor());
      
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