package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.Model.*;
import com.finalproject.tuwaiqfinal.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewSubHallService {

    private final ReviewSubHallRepository reviewSubHallRepository;
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final SubHallRepository subHallRepository;

    public List<ReviewSubHall> getAllReviewSubHall(){
        return reviewSubHallRepository.findAll();
    }

    public void AddReviewSubHall(Integer customer_id ,Integer subHall_id ,ReviewSubHall reviewSubHall){

        Customer customer = customerRepository.findCustomerById(customer_id);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        SubHall subHall = subHallRepository.findSubHallsById(subHall_id);
        if (subHall == null) {
            throw new ApiException("Sub Hall not found");
        }

        Booking booking = bookingRepository.findBookingsByCustomerAndSubHall(customer,subHall);
        if (booking == null) {
            throw new ApiException("customer does not have a booking");
        }

        reviewSubHall.setSubHall(subHall);

        reviewSubHallRepository.save(reviewSubHall);
    }
    public void UpdateReviewSubHall(Integer customer_id ,Integer subHall_id, Integer reviewSubHall_id ,ReviewSubHall reviewSubHall ){

        Customer customer = customerRepository.findCustomerById(customer_id);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        SubHall subHall = subHallRepository.findSubHallsById(subHall_id);
        if (subHall == null) {
            throw new ApiException("Sub Hall not found");
        }

        Booking booking = bookingRepository.findBookingsByCustomerAndSubHall(customer,subHall);
        if (booking == null) {
            throw new ApiException("customer does not have a booking");
        }
        ReviewSubHall oldreviewSubHall = reviewSubHallRepository.findReviewSubHallById(reviewSubHall_id);
        if (oldreviewSubHall == null){
            throw new ApiException("Review SubHall not found");
        }

        reviewSubHall.setSubHall(subHall);

        oldreviewSubHall.setComment(reviewSubHall.getComment());
        oldreviewSubHall.setRating(reviewSubHall.getRating());

        reviewSubHallRepository.save(oldreviewSubHall);
    }
    public void DeleteReviewSubHall(Integer customer_id, Integer subHall_id, Integer reviewSubHall_id){

        Customer customer = customerRepository.findCustomerById(customer_id);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        SubHall subHall = subHallRepository.findSubHallsById(subHall_id);
        if (subHall == null) {
            throw new ApiException("Sub Hall not found");
        }

        Booking booking = bookingRepository.findBookingsByCustomerAndSubHall(customer,subHall);
        if (booking == null) {
            throw new ApiException("customer does not have a booking");
        }

        ReviewSubHall reviewSubHall = reviewSubHallRepository.findReviewSubHallById(reviewSubHall_id);
        if (reviewSubHall == null){
            throw new ApiException("Review SubHall not found");
        }
        reviewSubHallRepository.delete(reviewSubHall);

    }


}
