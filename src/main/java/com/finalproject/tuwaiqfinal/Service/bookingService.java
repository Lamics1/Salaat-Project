package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.DTOin.BookingDTO;
import com.finalproject.tuwaiqfinal.DTOin.GameAvailabilityDTO;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class bookingService {
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final SubHallRepository subHallRepository;
    private final GameRepository gameRepository;
    private final MailService mailService;

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

        /// 3- Get available games for this time slot using our helper method
        List<GameAvailabilityDTO> availableGames = getAvailableGames(
                subHallId,
                bookingDTO.getStartAt(),
                bookingDTO.getDuration_minutes()
        );

        /// 4- Check if any games are available
        if(availableGames.isEmpty()) {
            throw new ApiException("No games available in this sub hall for the selected time slot");
        }

        /// 5- Auto-select the first available game
        // 5- Auto-select the first available game
        Game game = gameRepository.findById(availableGames.get(0).getGameId())
                .orElseThrow(() -> new ApiException("Selected game not found"));


        if(game == null) {
            throw new ApiException("Selected game not found");
        }
        /// 6- Validate team size
        if(game.getNumberOfPlayer() < bookingDTO.getMembers()) {
            throw new ApiException("maximum game's members is "+game.getNumberOfPlayer());
        }

        /// 7- calculate the end time of booking
        LocalDateTime endOfBooking = bookingDTO.getStartAt().plusMinutes(bookingDTO.getDuration_minutes());

        /// 8- Final conflict check
        List<Booking> conflicts = bookingRepository.findConflictBookingsForGame(
                game.getId(), bookingDTO.getStartAt(), endOfBooking);


        if(!conflicts.isEmpty()) {
            throw new ApiException("Game was just booked by someone else! Please try again");
        }

        /// 9- calculate price:
        double totalPrice = (bookingDTO.getDuration_minutes() * (subHall.getPricePerHour() / 60.0));

        ///  10- create & save booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setSubHall(subHall);
        booking.setStatus("initiated"); // not present at dto (initiated/cancelled/approved)
        booking.setMembers(bookingDTO.getMembers());
        booking.setDuration_minutes(bookingDTO.getDuration_minutes());
        booking.setTotalPrice(totalPrice);
        booking.setIsSplit(bookingDTO.getIsSplit());
        booking.setStartAt(bookingDTO.getStartAt());
        booking.setEndAt(endOfBooking);
        booking.setGame(game);
        bookingRepository.save(booking);

        /// - change isAvailable of game to (false) => reserved.
//        game.setIsAvailable(false);
//        gameRepository.save(game);

        /// 11- Send confirmation email
        String verifyUrl = "https://salat.com/verify?email=" + customer.getUser().getUsername();
        try {
            mailService.sendWelcomeHtml(customer.getUser().getEmail(), customer.getUser().getUsername(), verifyUrl);
        } catch
        (Exception ignored) {}

    }


    // 3- update booking by user (simplified - no subhall changes):
    public void updateBooking(Integer customerId,
                              Integer bookingId,
                              BookingDTO bookingDTO){

        /// 1- check if customer exist:
        Customer customer = customerRepository.findCustomerById(customerId);
        if(customer == null){
            throw new ApiException("customer not found");
        }

        /// 2- check if booking exist and belongs to customer:
        Booking oldBooking = bookingRepository.findBookingsById(bookingId);
        if(oldBooking == null) {
            throw new ApiException("booking not found");
        }
        if(!oldBooking.getCustomer().getId().equals(customerId)) {
            throw new ApiException("you are not authorized to update this booking");
        }

        /// 3- check if booking can be updated
        if(oldBooking.getStatus().equals("approved") || oldBooking.getStatus().equals("cancelled")) {
            throw new ApiException("cannot update approved or cancelled booking");
        }

        /// 4- get current game and subhall from existing booking
        Game currentGame = oldBooking.getGame();
        SubHall currentSubHall = currentGame.getSubHall();

        /// 5- use current subhall
        SubHall targetSubHall = currentSubHall;

        /// 6- calculate new end time
        LocalDateTime newEndTime = bookingDTO.getStartAt().plusMinutes(bookingDTO.getDuration_minutes());

        /// 7- check if current game is available for new time
        List<Booking> conflicts = bookingRepository.findConflictBookingsForGameExcludingBooking(
                currentGame.getId(),
                bookingDTO.getStartAt(),
                newEndTime,
                bookingId // exclude current booking from conflict check
        );

        if(!conflicts.isEmpty()) {
            throw new ApiException("current game is not available for the new time slot");
        }

        /// 8- validate team member count for current game
        if(currentGame.getNumberOfPlayer() < bookingDTO.getMembers()) {
            throw new ApiException("maximum game members is " + currentGame.getNumberOfPlayer());
        }

        /// 9- calculate new price
        double newTotalPrice = (bookingDTO.getDuration_minutes() * (targetSubHall.getPricePerHour() / 60.0));

        /// 10- update booking fields
        oldBooking.setMembers(bookingDTO.getMembers());
        oldBooking.setDuration_minutes(bookingDTO.getDuration_minutes());
        oldBooking.setTotalPrice(newTotalPrice);
        oldBooking.setIsSplit(bookingDTO.getIsSplit());
        oldBooking.setStartAt(bookingDTO.getStartAt());
        oldBooking.setEndAt(newEndTime);

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
        if(booking.getStatus().equals("cancelled"))
            throw new ApiException("booking already cancelled");


        /// 5- release game availability for this booking
        Game game = booking.getGame();
        if (game != null) {
            game.setIsAvailable(true);
            gameRepository.save(game);
        }

        /// 6- delete
        bookingRepository.delete(booking);
    }


    // 5- helper method for list available games
    public List<GameAvailabilityDTO> getAvailableGames(Integer subHallId,
                                                       LocalDateTime startTime,
                                                       Integer durationMinutes) {

        // 1- Check if subhall exists
        SubHall subHall = subHallRepository.findById(subHallId).orElse(null);
        if(subHall == null) {
            throw new ApiException("Sub hall not found");
        }

        // 2- Calculate end time
        LocalDateTime endTime = startTime.plusMinutes(durationMinutes);

        // 3- Get all games in this subhall
        List<Game> allGames = gameRepository.findAllBySubHallAndIsAvailable(subHall, true);

        // 4- Check availability for each game
        List<GameAvailabilityDTO> availableGames = new ArrayList<>();

        for(Game game : allGames) {
            List<Booking> conflicts = bookingRepository.findConflictBookingsForGame(
                    game.getId(), startTime, endTime
            );

            if(conflicts.isEmpty()) {
                GameAvailabilityDTO dto = new GameAvailabilityDTO();
                dto.setGameId(game.getId());
                dto.setColor(game.getColor());
                dto.setMaxPlayers(game.getNumberOfPlayer());
                dto.setAvailable(true);
                availableGames.add(dto);
            }
        }
        return availableGames;
    }
}
