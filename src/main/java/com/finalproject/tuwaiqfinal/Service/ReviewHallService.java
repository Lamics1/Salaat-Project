package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.Model.Booking;
import com.finalproject.tuwaiqfinal.Model.Customer;
import com.finalproject.tuwaiqfinal.Model.Hall;
import com.finalproject.tuwaiqfinal.Model.ReviewHall;
import com.finalproject.tuwaiqfinal.Repository.BookingRepository;
import com.finalproject.tuwaiqfinal.Repository.CustomerRepository;
import com.finalproject.tuwaiqfinal.Repository.HallRepository;
import com.finalproject.tuwaiqfinal.Repository.ReviewHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewHallService {

    private final ReviewHallRepository reviewHallRepository;
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final HallRepository hallRepository;

    public List<ReviewHall> getAllReviewHall(){
        return reviewHallRepository.findAll();
    }

    public void AddReviewHall(Integer customer_id, Integer hall_id, ReviewHall reviewHall) {

        Customer customer = customerRepository.findCustomerById(customer_id);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        Hall hall = hallRepository.findHallById(hall_id);
        if (hall == null) {
            throw new ApiException("Hall not found");
        }

        Booking booking = bookingRepository.findBookingsByCustomerId(customer.getId());
        if (booking == null) {
            throw new ApiException("customer does not have a booking");
        }
        reviewHall.setCustomer(customer);
        reviewHall.setHall(hall);

        reviewHallRepository.save(reviewHall);

    }

    public void UpdateReviewHall(Integer customer_id, Integer hall_id, Integer reviewHall_id, ReviewHall reviewHall) {

        Customer customer = customerRepository.findCustomerById(customer_id);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        Hall hall = hallRepository.findHallById(hall_id);
        if (hall == null) {
            throw new ApiException("Hall not found");
        }

        Booking booking = bookingRepository.findBookingsByCustomerId(customer.getId());
        if (booking == null) {
            throw new ApiException("customer does not have a booking");
        }

        ReviewHall oldReviewHall = reviewHallRepository.findReviewHallById(reviewHall_id);
        if (oldReviewHall == null) {
            throw new ApiException("Review Hall not found");
        }

        reviewHall.setCustomer(customer);
        reviewHall.setHall(hall);

        oldReviewHall.setRating(reviewHall.getRating());
        oldReviewHall.setComment(reviewHall.getComment());

        reviewHallRepository.save(oldReviewHall);
    }

    public void DeleteReviewHall(Integer customer_id, Integer hall_id, Integer reviewHall_id){

        Customer customer = customerRepository.findCustomerById(customer_id);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        Hall hall = hallRepository.findHallById(hall_id);
        if (hall == null) {
            throw new ApiException("Hall not found");
        }

        Booking booking = bookingRepository.findBookingsByCustomerId(customer.getId());
        if (booking == null) {
            throw new ApiException("customer does not have a booking");
        }

        ReviewHall reviewHall = reviewHallRepository.findReviewHallById(reviewHall_id);
        if (reviewHall == null) {
            throw new ApiException("Review Hall not found");
        }
        reviewHallRepository.delete(reviewHall);
    }

}
