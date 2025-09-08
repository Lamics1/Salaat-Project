package com.finalproject.tuwaiqfinal.DTOout;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PaymentDTO {

    private Integer id;
    private Double amount;
    private String currency;
    private String status;
    private LocalDateTime paid_at;
    private LocalDateTime created_at;

}
