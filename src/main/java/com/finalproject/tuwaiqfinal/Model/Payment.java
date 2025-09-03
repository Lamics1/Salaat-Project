package com.finalproject.tuwaiqfinal.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    @JsonIgnore
    private Booking booking;

    @NotNull(message = "Amount cannot be null")
    @Column(columnDefinition = "DECIMAL(10, 2)")
    private BigDecimal amount;

    @NotEmpty(message = "Currency cannot be empty")
    private String currency;

    @NotEmpty(message = "Status cannot be empty")
    private String status;

    @NotEmpty(message = "Provider cannot be empty")
    private String provider;

    @NotEmpty(message = "Provider reference cannot be empty")
    private String provider_ref;

    private Timestamp paid_at;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp created_at;
}
