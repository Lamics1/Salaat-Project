package com.finalproject.tuwaiqfinal.Controller;

import com.finalproject.tuwaiqfinal.Api.ApiResponse;
import com.finalproject.tuwaiqfinal.DTOout.AssetDTO;
import com.finalproject.tuwaiqfinal.DTOout.ReviewHallDTO;
import com.finalproject.tuwaiqfinal.DTOout.ReviewSubHallDTO;
import com.finalproject.tuwaiqfinal.Model.SubHall;
import com.finalproject.tuwaiqfinal.Model.User;
import com.finalproject.tuwaiqfinal.Service.SubhallService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/subhall")
@AllArgsConstructor
public class SubHallController {

    private final SubhallService subhallService;

    @GetMapping("/get/{subHallId}")
    public ResponseEntity<?> getSingleSubhall(@PathVariable Integer subHallId){
        return ResponseEntity.ok(subhallService.getSingleSubhall(subHallId));
    }

    @PostMapping("/add/{hallId}")
    public ResponseEntity<?> addSubHall(@AuthenticationPrincipal User user,@PathVariable Integer hallId, @RequestBody @Valid SubHall subHall){
        subhallService.addSubHall(user.getId(),hallId, subHall);
        return ResponseEntity.ok(new ApiResponse("SubHall has been added"));
    }

    @PutMapping("/update/{subHallId}")
    public ResponseEntity<?> updateSubHall(@AuthenticationPrincipal User user,@PathVariable Integer subHallId, @RequestBody @Valid SubHall subHall){
        subhallService.updateSubHall(user.getId(),subHallId, subHall);
        return ResponseEntity.ok(new ApiResponse("SubHall has been updated"));
    }

    @DeleteMapping("/delete/{subHallId}")
    public ResponseEntity<?> deleteSubhall(@AuthenticationPrincipal User user, @PathVariable Integer subHallId){
        subhallService.deleteSubhall(user.getId(), subHallId);
        return ResponseEntity.ok(new ApiResponse("SubHall has been deleted"));
    }

    @GetMapping("/hall/{hallId}/subhall/{subHallId}/budget/{pricePerHour}")
    public ResponseEntity<?> getSubHallsWithinBudget(@PathVariable Integer hallId, @PathVariable Integer subHallId, @PathVariable Integer pricePerHour) {
        List<ReviewSubHallDTO> list = subhallService.getSubHallsWithinBudget(hallId, subHallId, pricePerHour);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/add/asset/{subhallId}")
    public ResponseEntity<?> addAsset(@AuthenticationPrincipal User user, @PathVariable Integer subhallId, @RequestParam("file") MultipartFile file) throws IOException {
        subhallService.addAsset(subhallId,user.getId(),file);
        return ResponseEntity.ok(new ApiResponse("asset added"));
    }

    @GetMapping("/get/asset/{subhallId}")
    public ResponseEntity<byte[]> getAsset(@AuthenticationPrincipal User user, @PathVariable Integer subhallId) {
        AssetDTO asset = subhallService.getAsset(subhallId, user.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(asset.getContentType()));
        headers.setContentLength(asset.getData().length);

        return new ResponseEntity<>(asset.getData(), headers, HttpStatus.OK);
    }
}

