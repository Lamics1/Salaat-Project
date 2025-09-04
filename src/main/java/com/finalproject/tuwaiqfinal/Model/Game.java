package com.finalproject.tuwaiqfinal.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinColumn(name = "subHall_id")
    @JsonIgnore
    private SubHall subHall;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    @JsonIgnore
    private Booking booking;

    @NotEmpty(message = "Color cannot be empty")
    private String color;

    @NotNull(message = "isAvailable cannot be null")
    private Boolean isAvailable;

    @NotNull(message = "Number of players cannot be null")
    private Integer numberOfPlayer;
}
