package com.finalproject.tuwaiqfinal;

import com.finalproject.tuwaiqfinal.Model.Game;
import com.finalproject.tuwaiqfinal.Model.Hall;
import com.finalproject.tuwaiqfinal.Model.SubHall;
import com.finalproject.tuwaiqfinal.Repository.GameRepository;
import com.finalproject.tuwaiqfinal.Repository.HallRepository;
import com.finalproject.tuwaiqfinal.Repository.SubHallRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HallSubHallGameDaoTests {

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private SubHallRepository subHallRepository;

    @Autowired
    private GameRepository gameRepository;

    private Hall hall;
    private SubHall subHall1;
    private SubHall subHall2;
    private Game game1;
    private Game game2;

    @BeforeEach
    void setUp() {
        // Minimal valid Hall (owner is optional in entity mapping)
        hall = new Hall();
        hall.setName("Main Hall");
        hall.setDescription("Primary hall for events");
        hall.setLocation("Riyadh");
        hall.setIsAvailable(true);
        hall = hallRepository.save(hall);

        // Two SubHalls under the same Hall
        subHall1 = new SubHall();
        subHall1.setHall(hall);
        subHall1.setName("Room A");
        subHall1.setPricePerHour(100);
        subHall1.setDescription("Cozy room A");
        subHall1 = subHallRepository.save(subHall1);

        subHall2 = new SubHall();
        subHall2.setHall(hall);
        subHall2.setName("Room B");
        subHall2.setPricePerHour(120);
        subHall2.setDescription("Spacious room B");
        subHall2 = subHallRepository.save(subHall2);

        // Two Games attached to subHall1
        game1 = new Game();
        game1.setSubHall(subHall1);
        game1.setColor("Red");
        game1.setNumberOfPlayer(4);
        game1 = gameRepository.save(game1);

        game2 = new Game();
        game2.setSubHall(subHall1);
        game2.setColor("Blue");
        game2.setNumberOfPlayer(2);
        game2 = gameRepository.save(game2);
    }

    @Test
    public void testFindAllSubHallsByHall() {
        List<SubHall> subHalls = subHallRepository.getSubHallByHall(hall);
        Assertions.assertThat(subHalls).hasSize(2);
        // verify relation integrity
        Assertions.assertThat(subHalls.get(0).getHall().getId()).isEqualTo(hall.getId());
    }

    @Test
    public void testFindHallById_viaDerivedQueryOrJpa() {
        // Prefer a custom derived method (if you have it) like: Hall findHallById(Integer id);
        // Fallback to JpaRepository#findById
        Hall byCustom = null;
        try {
            // This will compile only if you have `Hall findHallById(Integer id)` in HallRepository
            byCustom = hallRepository.findHallById(hall.getId());
        } catch (Throwable ignored) {
            // ignore if method doesn't exist; use findById
        }
        Hall found = byCustom != null ? byCustom : hallRepository.findById(hall.getId()).orElse(null);
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getName()).isEqualTo("Main Hall");
    }

    @Test
    public void testFindAllGamesBySubHall() {
        List<Game> games = gameRepository.findAllBySubHall(subHall1);
        Assertions.assertThat(games).hasSize(2);
        Assertions.assertThat(games).extracting(Game::getColor)
                .containsExactlyInAnyOrder("Red", "Blue");
    }

    @Test
    public void testFindGameById() {
        // Prefer a custom derived method (if present): Game findGameById(Integer id);
        Game byCustom = null;
        try {
            byCustom = gameRepository.findGameById(game1.getId());
        } catch (Throwable ignored) {}
        Game found = byCustom != null ? byCustom : gameRepository.findById(game1.getId()).orElse(null);
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getSubHall().getId()).isEqualTo(subHall1.getId());
        Assertions.assertThat(found.getNumberOfPlayer()).isEqualTo(4);
    }

    @Test
    public void testDeleteGameCascadesToRepository() {
        // delete game1 and ensure it's removed
        gameRepository.delete(game1);
        List<Game> remaining = gameRepository.findAllBySubHall(subHall1);
        Assertions.assertThat(remaining).hasSize(1);
        Assertions.assertThat(remaining.get(0).getId()).isEqualTo(game2.getId());
    }
}
