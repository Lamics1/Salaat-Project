package com.finalproject.tuwaiqfinal.Controller;

import com.finalproject.tuwaiqfinal.Api.ApiResponse;
import com.finalproject.tuwaiqfinal.Model.Hall;
import com.finalproject.tuwaiqfinal.Model.User;
import com.finalproject.tuwaiqfinal.Service.HallService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hall")
@AllArgsConstructor
public class HallController {

    private final HallService hallService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllHalls(){
        return ResponseEntity.ok(hallService.getAllHalls());
    }

    @GetMapping("/get/{hallId}")
    public ResponseEntity<?> getSingleHall(@PathVariable Integer hallId){
        return ResponseEntity.ok(hallService.getSingleHall(hallId));
    }

    @GetMapping("/get/my")
    public ResponseEntity<?> getMyHalls(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(hallService.getMyHalls(user.getId()));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addHall(@AuthenticationPrincipal User user, @RequestBody @Valid Hall hall){
        hallService.addHall(user.getId(), hall);
        return ResponseEntity.ok(new ApiResponse("Hall has been added"));
    }

    @PutMapping("/update/{hallId}")
    public ResponseEntity<?> updateHall(@PathVariable Integer hallId, @AuthenticationPrincipal User user, @RequestBody @Valid Hall hall){
        hallService.updateHall(hallId, user.getId(), hall);
        return ResponseEntity.ok(new ApiResponse("Hall has been updated"));
    }

    @DeleteMapping("/delete/{hallId}")
    public ResponseEntity<?> deleteHall(@AuthenticationPrincipal User user,@PathVariable Integer hallId){
        hallService.deleteHall(user.getId(),hallId);
        return ResponseEntity.ok(new ApiResponse("Hall has been deleted"));
    }

    @GetMapping("/get-subhalls/{hallId}")
    public ResponseEntity<?> getAllSubHalls(@PathVariable Integer hallId){
        return ResponseEntity.ok(hallService.getAllSubHalls(hallId));
    }
}
