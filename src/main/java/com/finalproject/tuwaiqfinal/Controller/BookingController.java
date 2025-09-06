package com.finalproject.tuwaiqfinal.Controller;

import com.finalproject.tuwaiqfinal.Api.ApiResponse;
import com.finalproject.tuwaiqfinal.DTOin.BookingDTO;
import com.finalproject.tuwaiqfinal.Service.GameService;
import com.finalproject.tuwaiqfinal.Service.bookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {
    private final bookingService bookingService;

    @GetMapping("/get/customer/{customer_id}")
    public ResponseEntity<?> getBookingByCustomer(@PathVariable Integer customer_id) {
        return ResponseEntity.status(200).body(bookingService.getBookingByCustomer(customer_id));
    }

    @PostMapping("/add/customer/{customer_id}/subhall/{subhall_id}")
    public ResponseEntity<?> addBooking(@PathVariable Integer customer_id,
                                        @PathVariable Integer subhall_id,
                                        @Valid @RequestBody BookingDTO bookingDTO) {
        bookingService.addBooking(customer_id, subhall_id, bookingDTO);
        return ResponseEntity.status(200).body(new ApiResponse("booking added successfully"));
    }

    @PutMapping("/update/customer/{customer_id}/booking/{booking_id}")
    public ResponseEntity<?> updateBooking(@PathVariable Integer customer_id,
                                           @PathVariable Integer booking_id,
                                           @Valid @RequestBody BookingDTO bookingDTO) {
        bookingService.updateBooking(customer_id, booking_id, bookingDTO);
        return ResponseEntity.status(200).body(new ApiResponse("booking updated successfully"));
    }

    @DeleteMapping("/delete/customer/{customer_id}/booking/{booking_id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Integer customer_id,
                                           @PathVariable Integer booking_id) {
        bookingService.deleteBooking(customer_id, booking_id);
        return ResponseEntity.status(200).body(new ApiResponse("booking deleted successfully"));
    }

}