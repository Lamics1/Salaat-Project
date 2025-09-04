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
public class SubHall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    @JsonIgnore
    private Hall hall;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Price per hour cannot be null")
    private Integer pricePerHour;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @OneToMany(mappedBy = "subHall", cascade = CascadeType.ALL)
    private Set<Booking> bookings;

    @OneToMany(mappedBy = "subHall", cascade = CascadeType.ALL)
    private Set<Game> games;

    @OneToMany(mappedBy = "subHall", cascade = CascadeType.ALL)
    private Set<ReviewSubHall> reviewSubHalls;
}