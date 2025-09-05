package com.finalproject.tuwaiqfinal.Controller;

import com.finalproject.tuwaiqfinal.Api.ApiResponse;
import com.finalproject.tuwaiqfinal.DTOin.CustomerDTO;
import com.finalproject.tuwaiqfinal.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/get/{customer_id}")
    public ResponseEntity<?> getCustomer(@PathVariable Integer customer_id) {
        return ResponseEntity.status(200).body(customerService.getCustomer(customer_id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        customerService.addCustomer(customerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("customer added successfully"));
    }

    @PutMapping("/update/customer/{customer_id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer customer_id,
                                            @Valid @RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomer(customer_id,customerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("customer updated successfully"));
    }

    @DeleteMapping("/delete/customer/{customer_id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer customer_id) {
        customerService.deleteCustomer(customer_id);
        return ResponseEntity.status(200).body(new ApiResponse("customer deleted successfully"));
    }

    @GetMapping("/analyse")
    public ResponseEntity<?> analyseGame(@RequestBody byte[] image){
        return ResponseEntity.status(200).body(customerService.analyseGame(image));
    }
}
