package com.finalproject.tuwaiqfinal.Controller;
import com.finalproject.tuwaiqfinal.Api.ApiResponse;
import com.finalproject.tuwaiqfinal.Model.ReviewHall;
import com.finalproject.tuwaiqfinal.Service.ReviewHallService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/review-hall")
public class ReviewHallController {

    private final ReviewHallService reviewHallService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllReviewHall(){
        return ResponseEntity.status(200).body(reviewHallService.getAllReviewHall());
    }

    @PostMapping("/add/{customerId}/{hallId}")
    public ResponseEntity<?> AddReviewHall(@PathVariable Integer customerId,@PathVariable Integer hallId, @RequestBody @Valid ReviewHall reviewHall){
        reviewHallService.AddReviewHall(customerId, hallId, reviewHall);
        return ResponseEntity.status(200).body(new ApiResponse("ReviewHall has been added"));
    }

    @PutMapping("/update/{customerId}/{hallId}/{reviewHallId}")
    public ResponseEntity<?> updateHall(@PathVariable Integer customerId, @PathVariable Integer hallId,@PathVariable Integer reviewHallId,@RequestBody @Valid ReviewHall reviewHall){
        reviewHallService.UpdateReviewHall(customerId, hallId, reviewHallId,reviewHall);
        return ResponseEntity.status(200).body(new ApiResponse("ReviewHall has been updated"));
    }

    @DeleteMapping("/delete/{customerId}/{hallId}/{reviewHallId}")
    public ResponseEntity<?> deleteHall(@PathVariable Integer customerId,@PathVariable Integer hallId,@PathVariable Integer reviewHallId){
        reviewHallService.DeleteReviewHall(customerId,hallId,reviewHallId);
        return ResponseEntity.status(200).body(new ApiResponse("ReviewHall has been deleted"));
    }

}
