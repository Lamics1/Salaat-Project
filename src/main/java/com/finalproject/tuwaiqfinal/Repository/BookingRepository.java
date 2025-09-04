package com.finalproject.tuwaiqfinal.Repository;

import com.finalproject.tuwaiqfinal.Model.Booking;
import com.finalproject.tuwaiqfinal.Model.Customer;
import com.finalproject.tuwaiqfinal.Model.SubHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Booking findBookingsById(Integer id);

    Booking findBookingsByCustomerId(Integer customerId);

    Booking findBookingsByCustomerAndSubHall(Customer customer, SubHall subHall);
}
