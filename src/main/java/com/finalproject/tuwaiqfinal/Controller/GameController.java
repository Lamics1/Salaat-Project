package com.finalproject.tuwaiqfinal.Controller;
import com.finalproject.tuwaiqfinal.Api.ApiResponse;
import com.finalproject.tuwaiqfinal.Model.Game;
import com.finalproject.tuwaiqfinal.Model.User;
import com.finalproject.tuwaiqfinal.Service.GameService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/game")
public class GameController {

    private final GameService gameService;
    @GetMapping("/get")
    public ResponseEntity<?> getAllGame(){
        return ResponseEntity.status(200).body(gameService.getAllGame());
    }

    @PostMapping("/add/{hallId}/{subHallId}")
    public ResponseEntity<?> AddGame(@AuthenticationPrincipal User user, @PathVariable Integer hallId,@PathVariable Integer subHallId, @RequestBody @Valid Game game){
        gameService.AddGame(user.getId(), hallId, subHallId,game);
        return ResponseEntity.status(200).body(new ApiResponse("Game has been added"));
    }

    @PutMapping("/update/{hallId}/{subHallId}/{gameId}")
    public ResponseEntity<?> UpdateGame(@AuthenticationPrincipal User user, @PathVariable Integer hallId,@PathVariable Integer subHallId,@PathVariable Integer gameId,@RequestBody @Valid Game game){
        gameService.UpdateGame(user.getId(), hallId, subHallId,gameId,game);
        return ResponseEntity.status(200).body(new ApiResponse("Game has been updated"));
    }

    @DeleteMapping("/delete/{hallId}/{subHallId}/{gameId}")
    public ResponseEntity<?> DeleteGame(@AuthenticationPrincipal User user,@PathVariable Integer hallId,@PathVariable Integer subHallId,@PathVariable Integer gameId){
        gameService.DeleteGame(user.getId(),hallId,subHallId,gameId);
        return ResponseEntity.status(200).body(new ApiResponse("Game has been deleted"));
    }

}


