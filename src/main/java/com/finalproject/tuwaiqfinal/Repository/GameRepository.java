package com.finalproject.tuwaiqfinal.Repository;

import com.finalproject.tuwaiqfinal.Model.Booking;
import com.finalproject.tuwaiqfinal.Model.Game;
import com.finalproject.tuwaiqfinal.Model.SubHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    Game findGameById(Integer id);


    List<Game> findAllBySubHallAndIsAvailable(SubHall subHall, Boolean isAvailable);
}
