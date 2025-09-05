package com.finalproject.tuwaiqfinal.Controller;
import com.finalproject.tuwaiqfinal.Api.ApiResponse;
import com.finalproject.tuwaiqfinal.Model.ReviewSubHall;
import com.finalproject.tuwaiqfinal.Service.ReviewSubHallService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/review-sub-hall")
public class ReviewSubHallController {

    private final ReviewSubHallService reviewSubHallService;


    @GetMapping("/get")
    public ResponseEntity<?> getAllReviewSubHall(){
        return ResponseEntity.status(200).body(reviewSubHallService.getAllReviewSubHall());
    }

    @PostMapping("/add/{customerId}/{subHallId}")
    public ResponseEntity<?> AddReviewSubHall(@PathVariable Integer customerId, @PathVariable Integer subHallId, @RequestBody @Valid ReviewSubHall reviewSubHall){
        reviewSubHallService.AddReviewSubHall(customerId, subHallId, reviewSubHall);
        return ResponseEntity.status(200).body(new ApiResponse("Review Sub Hall has been added"));
    }

    @PutMapping("/update/{customerId}/{subHallId}/{ReviewSubHallId}")
    public ResponseEntity<?> UpdateReviewSubHall(@PathVariable Integer customerId, @PathVariable Integer subHallId,@PathVariable Integer ReviewSubHallId,@RequestBody @Valid ReviewSubHall reviewSubHall){
        reviewSubHallService.UpdateReviewSubHall(customerId, subHallId, ReviewSubHallId,reviewSubHall);
        return ResponseEntity.status(200).body(new ApiResponse("Review Sub Hall has been updated"));
    }

    @DeleteMapping("/delete/{customerId}/{subHallId}/{ReviewSubHallId}")
    public ResponseEntity<?> DeleteReviewSubHall(@PathVariable Integer customerId, @PathVariable Integer subHallId,@PathVariable Integer ReviewSubHallId){
        reviewSubHallService.DeleteReviewSubHall(customerId,subHallId,ReviewSubHallId);
        return ResponseEntity.status(200).body(new ApiResponse("Review Sub Hall has been deleted"));
    }

}

