package com.finalproject.tuwaiqfinal.Controller;

import com.finalproject.tuwaiqfinal.Api.ApiResponse;
import com.finalproject.tuwaiqfinal.DTOin.BookingDTO;
import com.finalproject.tuwaiqfinal.Model.Booking;
import com.finalproject.tuwaiqfinal.Service.GameService;
import com.finalproject.tuwaiqfinal.Service.bookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/initiated/owner/{ownerId}/hall/{hallId}")
    public ResponseEntity<?> getInitiatedByHall(@PathVariable Integer ownerId, @PathVariable Integer hallId) {
        List<Booking> bookings = bookingService.getInitiatedByHall(ownerId, hallId);
        return ResponseEntity.status(200).body(bookings);
    }

    @GetMapping("/Approved/owner/{ownerId}/hall/{hallId}")
    public ResponseEntity<?> getApprovedByHall(@PathVariable Integer ownerId, @PathVariable Integer hallId) {
        List<Booking> bookings = bookingService.getApprovedByHall(ownerId, hallId);
        return ResponseEntity.status(200).body(bookings);
    }

    @GetMapping("/remind-unpaid/{ownerId}/{hallId}")
    public ResponseEntity<?> remindUnpaid(@PathVariable Integer ownerId, @PathVariable Integer hallId) {
        bookingService.remindUnpaidByHall(ownerId, hallId);
        return ResponseEntity.status(200).body("message is send successfully");
    }

}