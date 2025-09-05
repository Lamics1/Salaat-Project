package com.finalproject.tuwaiqfinal.Repository;

import com.finalproject.tuwaiqfinal.Model.Game;
import com.finalproject.tuwaiqfinal.Model.SubHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    Game findGameById(Integer id);

    Game findGameBySubHall(SubHall subHall);

    Game findGameBySubHallAndIsAvailable(SubHall subHall, Boolean isAvailable);
}
