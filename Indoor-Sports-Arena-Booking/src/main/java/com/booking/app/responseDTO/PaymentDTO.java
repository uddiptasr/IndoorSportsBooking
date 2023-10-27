package com.booking.app.responseDTO;

import com.booking.app.model.enums.Transactionstatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String bookingStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID paymentId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String paymentmode;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int Totalamount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sportname;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int courtno;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String date;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Transactionstatus paymentGatewayStatus;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int change;

}
