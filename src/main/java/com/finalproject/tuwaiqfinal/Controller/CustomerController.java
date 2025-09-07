package com.finalproject.tuwaiqfinal.Controller;

import com.finalproject.tuwaiqfinal.Api.ApiResponse;
import com.finalproject.tuwaiqfinal.DTOin.CustomerDTO;
import com.finalproject.tuwaiqfinal.Model.User;
import com.finalproject.tuwaiqfinal.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;


    @GetMapping("/getall")
    public ResponseEntity<?> getAllCustomer() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }


    @GetMapping("/get")
    public ResponseEntity<?> getCustomer(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(customerService.getCustomer(user.getId()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        customerService.registerCustomer(customerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("customer added successfully"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCustomer(@AuthenticationPrincipal User user,
                                            @Valid @RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomer(user.getId(),customerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("customer updated successfully"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCustomer(@AuthenticationPrincipal User user) {
        customerService.deleteCustomer(user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("customer deleted successfully"));
    }

    @GetMapping("/analyse")
    public ResponseEntity<?> analyseGame(@RequestBody byte[] image){
        return ResponseEntity.status(200).body(customerService.analyseGame(image));
    }

    @DeleteMapping("/cancel/booking/{bookingId}")
    public ResponseEntity<?> cancelBooking(
            @AuthenticationPrincipal User user,
            @PathVariable Integer bookingId
    ) {
        customerService.customerCancelBooking(user.getId(), bookingId);
        return ResponseEntity.status(200).body(new ApiResponse("Booking has been cancelled successfully"));
    }
}
