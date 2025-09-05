package com.finalproject.tuwaiqfinal.DTOout;

import com.finalproject.tuwaiqfinal.Model.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreationResponseDTO {

    private Payment payment; // the base payment we will grab from it.
    private String transactionUrl;
    private String status;
    private String moyasarPaymentId; // Moyasar payment ID for reference
    private String message;

}
