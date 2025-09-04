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

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    private User user;

    @ManyToOne
    @JsonIgnore
    private Booking booking;

    @NotNull(message = "Amount cannot be null")
    @Column(columnDefinition = "DECIMAL(10, 2)", nullable = false)
    private BigDecimal amount;

    @NotEmpty(message = "Currency cannot be empty")
    @Column(nullable = false)
    private String currency;

    @NotEmpty(message = "Status cannot be empty")
    @Column(nullable = false)
    private String status;

    @NotEmpty(message = "Provider cannot be empty")
    @Column(nullable = false)
    private String provider;

    @NotEmpty(message = "Provider reference cannot be empty")
    @Column(nullable = false)
    private String provider_ref;

    private Timestamp paid_at;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp created_at;
}