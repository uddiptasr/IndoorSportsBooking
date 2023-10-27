package com.booking.app.responseDTO;

import com.booking.app.model.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private String bookingReferenceNo;//booking reference no
    private UUID paymentId;
    private BookingStatus bookingStatus;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int userId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String bookingCreationTimestamp;
    //private int courtSlotId;
    private String sportName;
    private int courtNo;
    private String startTime;
    private String endTime;
    private String date;
    private int totalCost;




}
