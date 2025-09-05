package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.DTOin.BookingDTO;
import com.finalproject.tuwaiqfinal.Model.Booking;
import com.finalproject.tuwaiqfinal.Model.Customer;
import com.finalproject.tuwaiqfinal.Model.Game;
import com.finalproject.tuwaiqfinal.Model.SubHall;
import com.finalproject.tuwaiqfinal.Repository.BookingRepository;
import com.finalproject.tuwaiqfinal.Repository.CustomerRepository;
import com.finalproject.tuwaiqfinal.Repository.GameRepository;
import com.finalproject.tuwaiqfinal.Repository.SubHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class bookingService {
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final SubHallRepository subHallRepository;
    private final GameRepository gameRepository;

    // 1- get all booking by one customer:
    public List<Booking> getBookingByCustomer(Integer customerId) {

        /// 1- check if customer exist:
        Customer customer = customerRepository.findCustomerById(customerId);
        if(customer == null) {
            throw new ApiException("customer not found");
        }
        return bookingRepository.findBookingsByCustomer_Id(customer.getId());
    }

    // 2- add booking by user to game at some sub hall:
    public void addBooking(Integer customerId,
                           Integer subHallId,
                           BookingDTO bookingDTO){

        /// 1- check if customer exist:
        Customer customer = customerRepository.findCustomerById(customerId);
        if(customer == null){
            throw new ApiException("customer not found");
        }

        /// 2- check if sub hall exist:
        SubHall subHall = subHallRepository.findSubHallsById(subHallId);
        if(subHall == null) {
            throw new ApiException("sub hall not found");
        }

        /// 3- find available game at this sub hall
        Game game = gameRepository.findGameBySubHallAndIsAvailable(subHall,true);
        if(game == null) {
            throw new ApiException("sub hall does not have game yet, or no game available at sub hall");
        }

        /// 4- validate from team member to a game
        if(game.getNumberOfPlayer() < bookingDTO.getMembers()) {
            throw new ApiException("maximum game's members is "+game.getNumberOfPlayer());
        }

        /// 5- calculate the end time of booking
        LocalDateTime endOfBooking = bookingDTO.getStartAt().plusMinutes(bookingDTO.getDuration_minutes());

        /// 6- check no booking interleaved with this booking
        List<Booking> conflictBooking = bookingRepository.findConflictBooking(subHall.getId(),bookingDTO.getStartAt(),endOfBooking);
        if(!conflictBooking.isEmpty()) {
            throw new ApiException("Time slot is already booked! Please choose a different time");
        }

        /// 7- calculate price:
        double totalPrice = (bookingDTO.getDuration_minutes() * (subHall.getPricePerHour() / 60.0));

        ///  8- create & save booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setSubHall(subHall);
        booking.setStatus("initiated"); // not present at dto
        booking.setMembers(bookingDTO.getMembers());
        booking.setDuration_minutes(bookingDTO.getDuration_minutes());
        booking.setTotalPrice(totalPrice);
        booking.setIsSplit(bookingDTO.getIsSplit());
        booking.setStartAt(bookingDTO.getStartAt());
        booking.setEndAt(endOfBooking);

        /// 9- change isAvailable of game to (false) => reserved.
        game.setIsAvailable(false);
        gameRepository.save(game);

        bookingRepository.save(booking);
    }



    // 3- update booking by user:
    public void updateBooking(Integer customerId,
                              Integer subHallId,
                              Integer bookingId,
                              BookingDTO bookingDTO){

        /// 1- check if customer exist:
        Customer customer = customerRepository.findCustomerById(customerId);
        if(customer == null){
            throw new ApiException("customer not found");
        }

        /// 2- check if sub hall exist:
        SubHall subHall = subHallRepository.findSubHallsById(subHallId);
        if(subHall == null) {
            throw new ApiException("sub hall not found");
        }

        /// 3- check if booking exist and belongs to customer:
        Booking oldBooking = bookingRepository.findBookingsById(bookingId);
        if(oldBooking == null) {
            throw new ApiException("booking not found");
        }
        if(!oldBooking.getCustomer().getId().equals(customerId)) {
            throw new ApiException("you are not authorized to update this booking");
        }

        /// 4- find available game at this sub hall
        Game game = gameRepository.findGameBySubHallAndIsAvailable(subHall,true);
        if(game == null) {
            throw new ApiException("sub hall does not have game yet, or no game available at sub hall");
        }

        /// 5- validate from team member to a game
        if(game.getNumberOfPlayer() < bookingDTO.getMembers()) {
            throw new ApiException("maximum game's members is "+game.getNumberOfPlayer());
        }

        /// 6- calculate the end time of booking
        LocalDateTime endOfBooking = bookingDTO.getStartAt().plusMinutes(bookingDTO.getDuration_minutes());

        /// 7- check no booking interleaved with this booking (exclude current booking)
        List<Booking> conflictBooking = bookingRepository.findConflictBooking(subHall.getId(),bookingDTO.getStartAt(),endOfBooking);
        if(!conflictBooking.isEmpty()) {
            throw new ApiException("Time slot is already booked! Please choose a different time");
        }

        /// 8- calculate price:
        double totalPrice = (bookingDTO.getDuration_minutes() * (subHall.getPricePerHour() / 60.0));

        /// 9- if sub hall changed, make old game available and new game unavailable
        if(!oldBooking.getSubHall().getId().equals(subHallId)) {
            // make old game available
            Game oldGame = gameRepository.findGameBySubHallAndIsAvailable(oldBooking.getSubHall(),false);
            if(oldGame != null) {
                oldGame.setIsAvailable(true);
                gameRepository.save(oldGame);
            }
            // make new game unavailable
            game.setIsAvailable(false);
            gameRepository.save(game);
        }

        /// 10- update booking fields
        oldBooking.setSubHall(subHall);
        oldBooking.setMembers(bookingDTO.getMembers());
        oldBooking.setDuration_minutes(bookingDTO.getDuration_minutes());
        oldBooking.setTotalPrice(totalPrice);
        oldBooking.setIsSplit(bookingDTO.getIsSplit());
        oldBooking.setStartAt(bookingDTO.getStartAt());
        oldBooking.setEndAt(endOfBooking);

        bookingRepository.save(oldBooking);
    }


    // 4- delete booking by customer
    public void deleteBooking(Integer customerId,Integer bookingId) {

        /// 1- check if booking exist
        Booking booking = bookingRepository.findBookingsById(bookingId);
        if(booking == null) {
            throw new ApiException("booking not found");
        }

        /// 2- check if customer exist
        Customer customer = customerRepository.findCustomerById(customerId);
        if(customer == null) {
            throw new ApiException("customer not found");
        }

        /// 3- check if booking belong to this customer:
        if(booking.getCustomer() == null || !booking.getCustomer().getId().equals(customer.getId())){
            throw new ApiException("customer not belong to this booking");
        }

        /// 4- check from booking status (no cancel after approve OR already cancelled )
        if(booking.getStatus().equals("approved"))
            throw new ApiException("booking approved, can't cancel this booking");
        if(booking.getStatus().equals("cancelled")) // or rejected
            throw new ApiException("booking already cancelled");


        /// 5- release game availability for this booking
        Game game = gameRepository.findByBooking(booking);
        if (game != null) {
            game.setIsAvailable(true);
            game.setBooking(null);
            gameRepository.save(game);
        }

        /// 6- delete
        bookingRepository.delete(booking);
    }
}
