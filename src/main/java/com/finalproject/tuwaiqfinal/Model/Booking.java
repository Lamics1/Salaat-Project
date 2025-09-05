package com.finalproject.tuwaiqfinal.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    private Customer customer;

    @ManyToOne
    @JsonIgnore
    private SubHall subHall;


    @NotEmpty(message = "Status cannot be empty")
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Integer members;

    @Column(nullable = false)
    private Integer duration_minutes;

    @NotNull(message = "Total price cannot be null")
    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private Boolean isSplit; // todo:delete this feature.

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @NotNull(message = "End time cannot be null")
    @Column(nullable = false)
    private LocalDateTime endAt;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private Set<Payment> payments;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private Set<Game> games;
}