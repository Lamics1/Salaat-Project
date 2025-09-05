package com.finalproject.tuwaiqfinal.Repository;

import com.finalproject.tuwaiqfinal.Model.Booking;
import com.finalproject.tuwaiqfinal.Model.Customer;
import com.finalproject.tuwaiqfinal.Model.SubHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findBookingsBySubHall(SubHall subHall);

    Booking findBookingsById(Integer id);

    Booking findBookingsByCustomerId(Integer customerId);

    Booking findBookingsByCustomerAndSubHall(Customer customer, SubHall subHall);

    List<Booking> findBookingsByCustomer_Id(Integer customerId);

    @Query("select b from Booking b where b.subHall.id=:subHall and " +
            "(b.startAt <: endAt AND b.endAt >: startAt) OR " +
            "(b.startAt >=:startAt AND b.startAt <:endAt)")
    List<Booking> findConflictBooking(Integer subHall,
                                   LocalDateTime startAt,
                                   LocalDateTime endAt);

}
