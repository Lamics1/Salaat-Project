package com.finalproject.tuwaiqfinal;

import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.Model.Owner;
import com.finalproject.tuwaiqfinal.Model.Customer;
import com.finalproject.tuwaiqfinal.Repository.OwnerRepository;
import com.finalproject.tuwaiqfinal.Repository.CustomerRepository;
import com.finalproject.tuwaiqfinal.Service.OwnerService;
import com.finalproject.tuwaiqfinal.Service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OwnerAndCustomerServiceTests {

    @InjectMocks
    OwnerService ownerService;

    @InjectMocks
    CustomerService customerService;

    @Mock
    OwnerRepository ownerRepository;

    @Mock
    CustomerRepository customerRepository;

    Owner owner1, owner2, owner3;
    List<Owner> owners;

    Customer customer1, customer2, customer3;
    List<Customer> customers;

    @BeforeEach
    void setUp() {
        owner1 = new Owner();
        owner1.setId(1);
        owner1.setAccount_serial_num("ACC001");

        owner2 = new Owner();
        owner2.setId(2);
        owner2.setAccount_serial_num("ACC002");

        owner3 = new Owner();
        owner3.setId(3);
        owner3.setAccount_serial_num("ACC003");

        // create list instead of real database:
        owners = new ArrayList<>();
        owners.add(owner1);
        owners.add(owner2);
        owners.add(owner3);

        // Customer setup
        customer1 = new Customer();
        customer1.setId(1);
        customer1.setPhone_number("0501234567");
        customer1.setLocation("Riyadh");
        customer1.setAge(25);

        customer2 = new Customer();
        customer2.setId(2);
        customer2.setPhone_number("0509876543");
        customer2.setLocation("Jeddah");
        customer2.setAge(30);

        customer3 = new Customer();
        customer3.setId(3);
        customer3.setPhone_number("0555555555");
        customer3.setLocation("Dammam");
        customer3.setAge(28);

        // create list instead of real database:
        customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);
    }

    /*
       1- we have specify (when & verify)
       2- each when comes with verify, not vise versa
       3- when we deal with repo, we assume its virtual database
     */

    // =============== Owner Service Tests ===============

    @Test
    public void getAllOwnersTest() {
        // 1- mock behavior from (owner repo)
        // 2- result of (findAll()) is saved at owners that created in @BeforeEach.
        when(ownerRepository.findAll()).thenReturn(owners);

        // 1- call the actual method to test it.
        List<Owner> ownerList = ownerService.getAllOwners();
        Assertions.assertEquals(3, ownerList.size());
        Assertions.assertEquals(owners, ownerList);

        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    public void getSingleOwnerTest() {
        // mock the findById to return Optional with owner1
        when(ownerRepository.findById(owner1.getId())).thenReturn(Optional.of(owner1));

        // call the actual method to test it
        Owner result = ownerService.getSingleOwner(owner1.getId());

        Assertions.assertEquals(owner1, result);
        Assertions.assertEquals("ACC001", result.getAccount_serial_num());

        verify(ownerRepository, times(1)).findById(owner1.getId());
    }

    @Test
    public void getSingleOwnerNotFoundTest() {
        // mock the findById to return empty Optional (owner not found)
        when(ownerRepository.findById(999)).thenReturn(Optional.empty());

        // test that ApiException is thrown
        ApiException exception = assertThrows(ApiException.class, () -> {
            ownerService.getSingleOwner(999);
        });

        Assertions.assertEquals("owner not found", exception.getMessage());

        verify(ownerRepository, times(1)).findById(999);
    }

    // =============== Customer Service Tests ===============

    @Test
    public void getAllCustomersTest() {
        // 1- mock behavior from (customer repo)
        // 2- result of (findAll()) is saved at customers that created in @BeforeEach.
        when(customerRepository.findAll()).thenReturn(customers);

        // 1- call the actual method to test it.
        List<Customer> customerList = customerService.getAllCustomers();
        Assertions.assertEquals(3, customerList.size());
        Assertions.assertEquals(customers, customerList);

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void getCustomerTest() {
        // mock the findCustomerById to return customer1
        when(customerRepository.findCustomerById(customer1.getId())).thenReturn(customer1);

        // call the actual method to test it
        Customer result = customerService.getCustomer(customer1.getId());

        Assertions.assertEquals(customer1, result);
        Assertions.assertEquals("0501234567", result.getPhone_number());
        Assertions.assertEquals("Riyadh", result.getLocation());
        Assertions.assertEquals(25, result.getAge());

        verify(customerRepository, times(1)).findCustomerById(customer1.getId());
    }

    @Test
    public void getCustomerNotFoundTest() {
        // mock the findCustomerById to return null (customer not found)
        when(customerRepository.findCustomerById(999)).thenReturn(null);

        // test that ApiException is thrown
        ApiException exception = assertThrows(ApiException.class, () -> {
            customerService.getCustomer(999);
        });

        Assertions.assertEquals("customer not found", exception.getMessage());

        verify(customerRepository, times(1)).findCustomerById(999);
    }
}