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

import java.sql.Timestamp;
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
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "subHall_id")
    @JsonIgnore
    private SubHall subHall;

    @NotEmpty(message = "Status cannot be empty")
    private String status;

    @NotNull(message = "Members cannot be null")
    private Integer members;

    @NotNull(message = "Duration cannot be null")
    private Integer duration_minutes;

    @NotNull(message = "Total price cannot be null")
    private Integer totalPrice;

    @NotNull(message = "isSplit cannot be null")
    private Boolean isSplit;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp created_at;

    @NotNull(message = "Start time cannot be null")
    private Timestamp startAt;

    @NotNull(message = "End time cannot be null")
    private Timestamp endAt;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private Set<Payment> payments;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private Set<Game> games;
}