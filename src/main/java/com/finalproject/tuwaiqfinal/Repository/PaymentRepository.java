package com.finalproject.tuwaiqfinal.Repository;

import com.finalproject.tuwaiqfinal.Model.Booking;
import com.finalproject.tuwaiqfinal.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findByMoyasarPaymentId(String moyasarPaymentId);

    Payment findByBooking(Booking booking);

    Payment getPaymentByMoyasarPaymentId(String moyasarPaymentId);

    List<Payment> findAllByCustomerId(Integer customerId);

    List<Payment> findAllByCustomerIdAndStatus(Integer customerId, String status);
}
