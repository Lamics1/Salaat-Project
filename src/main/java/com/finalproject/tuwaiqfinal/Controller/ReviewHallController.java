package com.finalproject.tuwaiqfinal.Controller;
import com.finalproject.tuwaiqfinal.Api.ApiResponse;
import com.finalproject.tuwaiqfinal.DTOout.ReviewHallDTO;
import com.finalproject.tuwaiqfinal.Model.ReviewHall;
import com.finalproject.tuwaiqfinal.Model.User;
import com.finalproject.tuwaiqfinal.Service.ReviewHallService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/review-hall")
public class ReviewHallController {

    private final ReviewHallService reviewHallService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllReviewHall(){
        return ResponseEntity.status(200).body(reviewHallService.getAllReviewHall());
    }

    @PostMapping("/add/{hallId}")
    public ResponseEntity<?> AddReviewHall(@AuthenticationPrincipal User user,@PathVariable Integer hallId, @RequestBody @Valid ReviewHall reviewHall){
        reviewHallService.AddReviewHall(user.getId(), hallId, reviewHall);
        return ResponseEntity.status(200).body(new ApiResponse("ReviewHall has been added"));
    }

    @PutMapping("/update/{hallId}/{reviewHallId}")
    public ResponseEntity<?> updateHall(@AuthenticationPrincipal User user, @PathVariable Integer hallId,@PathVariable Integer reviewHallId,@RequestBody @Valid ReviewHall reviewHall){
        reviewHallService.UpdateReviewHall(user.getId(), hallId, reviewHallId,reviewHall);
        return ResponseEntity.status(200).body(new ApiResponse("ReviewHall has been updated"));
    }

    @DeleteMapping("/delete/{hallId}/{reviewHallId}")
    public ResponseEntity<?> deleteHall(@AuthenticationPrincipal User user,@PathVariable Integer hallId,@PathVariable Integer reviewHallId){
        reviewHallService.DeleteReviewHall(user.getId(),hallId,reviewHallId);
        return ResponseEntity.status(200).body(new ApiResponse("ReviewHall has been deleted"));
    }
    @GetMapping("/hall/{hallId}/rating")
    public ResponseEntity<?> getHallReviewsByMaxRating(@PathVariable Integer hallId) {
        List<ReviewHallDTO> list = reviewHallService.getHallReviewsByMaxRating(hallId);
        return ResponseEntity.status(200).body(list);
    }

}
