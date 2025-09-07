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
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    private SubHall subHall;

    // change relation
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "game")
    private Set<Booking> bookings;

    @NotEmpty(message = "Color cannot be empty")
    @Column(nullable = false)
    private String color;

    @NotNull(message = "isAvailable cannot be null")
    @Column(nullable = false)
    private Boolean isAvailable;

    @NotNull(message = "Number of players cannot be null")
    @Column(nullable = false)
    private Integer numberOfPlayer;
}
