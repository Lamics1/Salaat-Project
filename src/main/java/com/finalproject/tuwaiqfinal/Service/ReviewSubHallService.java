package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.Model.*;
import com.finalproject.tuwaiqfinal.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import com.finalproject.tuwaiqfinal.DTOout.AssetDTO;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewSubHallService {

    private final ReviewSubHallRepository reviewSubHallRepository;
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final SubHallRepository subHallRepository;
    private final S3Service s3Service;

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

    public void addAsset(Integer reviewId, Integer customerId, MultipartFile image) throws IOException {
        ReviewSubHall reviewSubHall = reviewSubHallRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException("Review not found"));

        if (!reviewSubHall.getCustomer().getId().equals(customerId)) {
            throw new ApiException("Permission denied");
        }

        if (image.isEmpty()) {
            throw new ApiException("Cannot upload an empty file");
        }

        String extension = FilenameUtils.getExtension(image.getOriginalFilename());
        if (!"png".equals(extension) && !"jpg".equals(extension) && !"jpeg".equals(extension)) {
            throw new ApiException("Only PNG, JPG, and JPEG files are allowed");
        }

        String s3Key = "reviews/subhalls/" + reviewSubHall.getId() + "/" + UUID.randomUUID().toString() + "." + extension;
        reviewSubHall.setImageURL(s3Service.uploadFile(s3Key, image));
        reviewSubHallRepository.save(reviewSubHall);
    }

    public AssetDTO getAsset(Integer reviewId) {
        ReviewSubHall reviewSubHall = reviewSubHallRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException("Review not found"));

        if (reviewSubHall.getImageURL() == null) {
            throw new ApiException("Review has no image");
        }

        try {
            URL url = new URL(reviewSubHall.getImageURL());
            String key = url.getPath().substring(1);
            byte[] data = s3Service.downloadBytes(key);

            String contentType = "application/octet-stream"; // Default
            if (key.toLowerCase().endsWith(".png")) {
                contentType = "image/png";
            } else if (key.toLowerCase().endsWith(".jpg") || key.toLowerCase().endsWith(".jpeg")) {
                contentType = "image/jpeg";
            }

            return new AssetDTO(data, contentType);
        } catch (MalformedURLException e) {
            throw new ApiException("Invalid image URL: " + e.getMessage());
        }
    }
}