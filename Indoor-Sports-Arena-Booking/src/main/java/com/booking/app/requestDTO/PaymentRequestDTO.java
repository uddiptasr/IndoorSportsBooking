package com.booking.app.requestDTO;

import com.booking.app.model.enums.Transactionstatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    //uid

    @DecimalMin(message = "Please enter the amount", value = "1")
    private int amount;
    @NotEmpty(message = "Please enter the bookingReferenceNo")
    private String bookingReferenceNo;
    @NotEmpty(message = "Please enter the paymentMode")
    private String paymentMode;
    @NotNull(message = "Please enter the transactionStatus")
    private Transactionstatus transactionStatus;//enum
}
//uid//created date