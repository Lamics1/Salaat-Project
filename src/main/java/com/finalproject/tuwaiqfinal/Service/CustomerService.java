package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.DTOin.CustomerDTO;
import com.finalproject.tuwaiqfinal.DTOout.AnalyseGameDTO;
import com.finalproject.tuwaiqfinal.Model.Booking;
import com.finalproject.tuwaiqfinal.Model.Customer;
import com.finalproject.tuwaiqfinal.Model.Game;
import com.finalproject.tuwaiqfinal.Model.User;
import com.finalproject.tuwaiqfinal.Repository.BookingRepository;
import com.finalproject.tuwaiqfinal.Repository.CustomerRepository;
import com.finalproject.tuwaiqfinal.Repository.GameRepository;
import com.finalproject.tuwaiqfinal.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
//  repos DI
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

//  services DI
    private final AiService aiService;
    private final WhatsAppService whatsAppService;
    private final BookingRepository bookingRepository;
    private final GameRepository gameRepository;

    /// 1- get customer by his id
    public Customer getCustomer(Integer customerId){

        /// 2- check if customer exists:
        Customer customer = customerRepository.findCustomerById(customerId);
        if(customer == null){
            throw new ApiException("customer not found");
        }

        // return customer:
        return customer;
    }


    // 2- add customer:
    public void addCustomer(CustomerDTO customerDTO){

        /// 1- add values for user model
        User user  = new User();

        user.setUsername(customerDTO.getUsername());
        user.setPassword(customerDTO.getPassword());
        user.setEmail(customerDTO.getEmail());
        user.setRole("CUSTOMER");

        /// 2- add values for customer model
        Customer customer =
                new Customer(null
                        ,customerDTO.getPhoneNumber()
                        ,customerDTO.getLocation()
                        ,customerDTO.getAge()
                        ,user
                        ,null
                        ,null
                        ,null
                        ,null);


        ///  4- save user
        userRepository.save(user);

        /// 3- save customer
        customerRepository.save(customer);

        whatsAppService.sendText(customerDTO.getPhoneNumber(),"اهلا وسهلا, ",customerDTO.getUsername()," .نورت موقع صالات عندنا تقدر تكتشف كل الصالات القريبة منك , وتتصفح الألعاب المتوفرة , ,وتشوف الأسعار وتحجز بكل سهولة . جاهز تختار صالتك وتعيش الجو ؟ ");
    }


    // 3- update customer
    public void updateCustomer(Integer customerId, CustomerDTO dto){

        /// 1- Find existing customer
        Customer oldCustomer = customerRepository.findCustomerById(customerId);
        if(oldCustomer == null) {
            throw new ApiException("Customer not found");
        }

        // 2- Get the associated user
        User user = oldCustomer.getUser();
        if(user == null) {
            throw new ApiException("Associated user not found");
        }

        /// 3- Update user fields
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // Consider hashing this
        user.setEmail(dto.getEmail());

        /// 4- Update customer fields
        oldCustomer.setPhone_number(dto.getPhoneNumber());
        oldCustomer.setLocation(dto.getLocation());
        oldCustomer.setAge(dto.getAge());

        /// 5- Save customer first
        customerRepository.save(oldCustomer);

        /// 6- Save user
        userRepository.save(user);
    }


    // 5- delete customer
    public void deleteCustomer(Integer customerId) {

        /// 1- check if customer exist:
        Customer customer = customerRepository.findCustomerById(customerId);
        if(customer == null) {
            throw new ApiException("customer not found");
        }

        /// 2- delete customer:
        customerRepository.delete(customer);

        // hence, no need for deleting booking that related with customer.
        /// 3- delete parent:
        userRepository.delete(customer.getUser());
    }

    public AnalyseGameDTO analyseGame(byte[] gameImage){
        return aiService.analyzeImage(gameImage);
    }

     /*
        1- for cancel booking, we have to delete the booking from the game also
     */
    // 6- allow customer to cancel their booking & delete booking
    public void customerCancelBooking(Integer customerId,Integer bookingId) {

        // check if user exist
       Customer customer = customerRepository.findCustomerById(customerId);
       if(customer == null) {
           throw new ApiException("customer not found");
       }

       // check if booking exist
        Booking booking = bookingRepository.findBookingsById(bookingId);
       if(booking == null) {
           throw new ApiException("booking not found");
       }

       // check if customer belong to this booking
        if(booking.getCustomer() == null || !booking.getCustomer().getId().equals(customerId)) {
            throw new ApiException("customer not belong to this booking");
        }

        if(booking.getStatus().equalsIgnoreCase("approved")) {
            throw new ApiException("booking handled before");
        }

        // delete booking form game:
        bookingRepository.delete(booking);

    }


}
