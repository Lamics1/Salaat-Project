package com.finalproject.tuwaiqfinal.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {

    @Id
    private Integer id;

    @NotEmpty(message = "Phone number cannot be empty")
    @Column(nullable = false, unique = true)
    private String phone_number;


    //todo: lat, lon
    @NotEmpty(message = "Location cannot be empty")
    @Column(nullable = false)
    private String location;

    @NotNull(message = "Age cannot be null")
    @Column(nullable = false)
    private Integer age;

    //relations

    @OneToOne
    @MapsId
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Booking> bookings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Payment> payments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<ReviewHall> reviewHalls;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<ReviewSubHall> reviewSubHalls;
}