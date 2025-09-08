package com.finalproject.tuwaiqfinal.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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
    @JsonIgnore
    private Hall hall;

    @NotEmpty(message = "Name cannot be empty")
    @Column(nullable = false)
    private String name;

    private String imageURL;

    @NotNull(message = "Price per hour cannot be null")
    @Column(nullable = false)
    private Integer pricePerHour;

    @NotEmpty(message = "Description cannot be empty")
    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "subHall", cascade = CascadeType.ALL)
    private Set<Booking> bookings;

    @OneToMany(mappedBy = "subHall", cascade = CascadeType.ALL)
    private Set<Game> games;

    @OneToMany(mappedBy = "subHall", cascade = CascadeType.ALL)
    private Set<ReviewSubHall> reviewSubHalls;
}