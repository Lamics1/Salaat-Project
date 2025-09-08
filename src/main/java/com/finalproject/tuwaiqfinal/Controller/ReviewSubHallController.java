package com.finalproject.tuwaiqfinal.Controller;
import com.finalproject.tuwaiqfinal.Api.ApiResponse;
import com.finalproject.tuwaiqfinal.Model.ReviewSubHall;
import com.finalproject.tuwaiqfinal.Model.User;
import com.finalproject.tuwaiqfinal.Service.ReviewSubHallService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.finalproject.tuwaiqfinal.DTOout.AssetDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/review-sub-hall")
public class ReviewSubHallController {

    private final ReviewSubHallService reviewSubHallService;


    @GetMapping("/get")
    public ResponseEntity<?> getAllReviewSubHall(){
        return ResponseEntity.status(200).body(reviewSubHallService.getAllReviewSubHall());
    }

    @PostMapping("/add/{subHallId}")
    public ResponseEntity<?> AddReviewSubHall(@AuthenticationPrincipal User user, @PathVariable Integer subHallId, @RequestBody @Valid ReviewSubHall reviewSubHall){
        reviewSubHallService.AddReviewSubHall(user.getId(), subHallId, reviewSubHall);
        return ResponseEntity.status(200).body(new ApiResponse("Review Sub Hall has been added"));
    }

    @PutMapping("/update/{subHallId}/{ReviewSubHallId}")
    public ResponseEntity<?> UpdateReviewSubHall(@AuthenticationPrincipal User user, @PathVariable Integer subHallId,@PathVariable Integer ReviewSubHallId,@RequestBody @Valid ReviewSubHall reviewSubHall){
        reviewSubHallService.UpdateReviewSubHall(user.getId(), subHallId, ReviewSubHallId,reviewSubHall);
        return ResponseEntity.status(200).body(new ApiResponse("Review Sub Hall has been updated"));
    }

    @DeleteMapping("/delete/{subHallId}/{ReviewSubHallId}")
    public ResponseEntity<?> DeleteReviewSubHall(@AuthenticationPrincipal User user, @PathVariable Integer subHallId,@PathVariable Integer ReviewSubHallId){
        reviewSubHallService.DeleteReviewSubHall(user.getId(),subHallId,ReviewSubHallId);
        return ResponseEntity.status(200).body(new ApiResponse("Review Sub Hall has been deleted"));
    }

    @PutMapping("/add/asset/{reviewId}")
    public ResponseEntity<?> addAsset(@AuthenticationPrincipal User user, @PathVariable Integer reviewId, @RequestParam("file") MultipartFile file) throws IOException {
        reviewSubHallService.addAsset(reviewId, user.getId(), file);
        return ResponseEntity.ok(new ApiResponse("asset added"));
    }

    @GetMapping("/get/asset/{reviewId}")
    public ResponseEntity<byte[]> getAsset(@PathVariable Integer reviewId) {
        AssetDTO asset = reviewSubHallService.getAsset(reviewId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(asset.getContentType()));
        headers.setContentLength(asset.getData().length);

        return new ResponseEntity<>(asset.getData(), headers, HttpStatus.OK);
    }

}

