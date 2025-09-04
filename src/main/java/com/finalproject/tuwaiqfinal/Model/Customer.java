package com.finalproject.tuwaiqfinal.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {

    @Id
    private Integer id;

    @NotEmpty(message = "Phone number cannot be empty")
    private String phone_number;

    @NotEmpty(message = "Location cannot be empty")
    private String location;

    @NotNull(message = "Age cannot be null")
    private Integer age;

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Booking> bookings;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Payment> payments;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<ReviewHall> reviewHalls;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<ReviewSubHall> reviewSubHalls;
}
