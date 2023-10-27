package com.booking.app.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtSlotsRequestDTO {
    private int userId;
    //@NotEmpty(message = "Please enter the courtNo ")
    @DecimalMin(message = "Please enter the courtNo", value = "1")
    private int courtNumber;

    private int courtId;
    private int courtSlotId;
    @DecimalMin(message = "Please enter the sportId", value = "1")
    private int sportId;
    @NotEmpty(message = "Please enter the startTime")
    private String StartTime;
    @NotEmpty(message = "Please enter the endtime")
    private String EndTime;
    @NotEmpty(message = "Please enter the date")
    private String date;



}
/*
*
    {
        "userId":2,
        "courtNumber": 3,
        "courtId":3,
        "courtSlotId":0,
        "sportId": 1,
        "startTime": "12:45",
        "endTime": "13:15",
         "date": "15-04-2021"
    }

    {
        "userId":2,
        "sportId": 1,
        "courtNumber": 3,
        "date": "15-04-2021",
        "startTime": "12:45",
        "endTime": "13:15"
    }

* */
