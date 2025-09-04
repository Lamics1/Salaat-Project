package com.finalproject.tuwaiqfinal.Controller;

import com.finalproject.tuwaiqfinal.Api.ApiResponse;
import com.finalproject.tuwaiqfinal.DTOin.OwnerDTO;
import com.finalproject.tuwaiqfinal.Model.Owner;
import com.finalproject.tuwaiqfinal.Service.OwnerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/owner")
@AllArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllOwners(){
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    @GetMapping("/get/{ownerId}")
    public ResponseEntity<?> getSingleOwner(@PathVariable Integer ownerId){
        return ResponseEntity.ok(ownerService.getSingleOwner(ownerId));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerOwner(@RequestBody @Valid OwnerDTO ownerDTO){
        ownerService.registerOwner(ownerDTO);
        return ResponseEntity.ok(new ApiResponse("owner have been registered"));
    }

    @PutMapping("/update/{ownerId}")
    public ResponseEntity<?> updateOwner(@PathVariable Integer ownerId, @RequestBody @Valid OwnerDTO ownerDTO){
        ownerService.updateOwner(ownerId,ownerDTO);
        return ResponseEntity.ok(new ApiResponse("owner have been updated"));
    }

    @DeleteMapping("/delete/{ownerId}")
    public ResponseEntity<?> deleteOwner(@PathVariable Integer ownerId){
        ownerService.deleteOwner(ownerId);
        return ResponseEntity.ok(new ApiResponse("owner have been deleted"));
    }
}
