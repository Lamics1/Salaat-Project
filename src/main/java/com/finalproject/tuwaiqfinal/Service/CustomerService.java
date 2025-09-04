package com.finalproject.tuwaiqfinal.Service;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.DTOin.CustomerDTO;
import com.finalproject.tuwaiqfinal.Model.Customer;
import com.finalproject.tuwaiqfinal.Model.User;
import com.finalproject.tuwaiqfinal.Repository.CustomerRepository;
import com.finalproject.tuwaiqfinal.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

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

        /// 3- save customer
        customerRepository.save(customer);

        ///  4- save user
        userRepository.save(user);
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


}
