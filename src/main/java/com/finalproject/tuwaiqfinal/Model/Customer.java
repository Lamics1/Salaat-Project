package com.finalproject.tuwaiqfinal.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Check(constraints = "age >= 12")
public class Customer {

    @Id
    private Integer id;

    @Column(nullable = false, unique = true)
    private String phone_number;


    //todo: lat, lon
    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer age;


    //relations

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