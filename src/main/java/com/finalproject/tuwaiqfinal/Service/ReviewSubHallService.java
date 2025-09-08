package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.Model.*;
import com.finalproject.tuwaiqfinal.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewSubHallService {

    private final ReviewSubHallRepository reviewSubHallRepository;
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final SubHallRepository subHallRepository;

    public List<ReviewSubHall> getAllReviewSubHall() {
        return reviewSubHallRepository.findAll();
    }

    public void AddReviewSubHall(Integer customer_id, Integer subHall_id, ReviewSubHall reviewSubHall) {

        Customer customer = customerRepository.findCustomerById(customer_id);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        SubHall subHall = subHallRepository.findSubHallsById(subHall_id);
        if (subHall == null) {
            throw new ApiException("Sub Hall not found");
        }

        Booking booking = bookingRepository.findBookingsByCustomerAndSubHallAndStatus(customer, subHall,"approved");
        if (booking == null) {
            throw new ApiException("customer does not have a booking");
        }
        //Prevent duplicate review
        if (reviewSubHallRepository.existsByCustomerIdAndSubHallId(customer.getId(), subHall.getId())) {
            throw new ApiException("duplicate review not allowed");
        }

        if (!"approved".equalsIgnoreCase(booking.getStatus())) {
            throw new ApiException("booking must be approved to review this sub hall");
        }
//
//        if (booking.getEndAt() != null && booking.getEndAt().isAfter(LocalDateTime.now())) {
//            throw new ApiException("you can review after your session ends");
//        }
        reviewSubHall.setSubHall(subHall);
        reviewSubHall.setCustomer(customer);

        reviewSubHallRepository.save(reviewSubHall);
    }

    public void UpdateReviewSubHall(Integer customer_id, Integer subHall_id, Integer reviewSubHall_id, ReviewSubHall reviewSubHall) {

        Customer customer = customerRepository.findCustomerById(customer_id);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        SubHall subHall = subHallRepository.findSubHallsById(subHall_id);
        if (subHall == null) {
            throw new ApiException("Sub Hall not found");
        }

        Booking booking = bookingRepository.findBookingsByCustomerAndSubHallAndStatus(customer, subHall,"approved");
        if (booking == null) {
            throw new ApiException("customer does not have a booking");
        }
        ReviewSubHall oldreviewSubHall = reviewSubHallRepository.findReviewSubHallById(reviewSubHall_id);
        if (oldreviewSubHall == null) {
            throw new ApiException("Review SubHall not found");
        }

        if (oldreviewSubHall.getCustomer() == null || !oldreviewSubHall.getCustomer().getId().equals(customer.getId())) {
            throw new ApiException("Forbidden: cannot modify another customer's review");
        }

        if (oldreviewSubHall.getSubHall() == null || !oldreviewSubHall.getSubHall().getId().equals(subHall.getId())) {
            throw new ApiException("Review does not belong to this Sub Hall");
        }

        reviewSubHall.setSubHall(subHall);
        reviewSubHall.setCustomer(customer);

        oldreviewSubHall.setComment(reviewSubHall.getComment());
        oldreviewSubHall.setRating(reviewSubHall.getRating());

        reviewSubHallRepository.save(oldreviewSubHall);
    }

    public void DeleteReviewSubHall(Integer customer_id, Integer subHall_id, Integer reviewSubHall_id) {

        Customer customer = customerRepository.findCustomerById(customer_id);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        SubHall subHall = subHallRepository.findSubHallsById(subHall_id);
        if (subHall == null) {
            throw new ApiException("Sub Hall not found");
        }

        Booking booking = bookingRepository.findBookingsByCustomerAndSubHallAndStatus(customer, subHall,"approved");
        if (booking == null) {
            throw new ApiException("customer does not have an approved booking");
        }

        ReviewSubHall reviewSubHall = reviewSubHallRepository.findReviewSubHallById(reviewSubHall_id);
        if (reviewSubHall == null) {
            throw new ApiException("Review SubHall not found");
        }

        if (reviewSubHall.getCustomer() == null || !reviewSubHall.getCustomer().getId().equals(customer.getId())) {
            throw new ApiException("Forbidden: cannot delete another customer's review");
        }
        if (reviewSubHall.getSubHall() == null || !reviewSubHall.getSubHall().getId().equals(subHall.getId())) {
            throw new ApiException("Review does not belong to this Sub Hall");
        }
        reviewSubHallRepository.delete(reviewSubHall);

    }
}