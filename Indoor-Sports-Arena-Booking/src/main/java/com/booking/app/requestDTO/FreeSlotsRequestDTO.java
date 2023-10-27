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
public class FreeSlotsRequestDTO {


    @DecimalMin(message = "Please enter the sportId", value = "1")
    private int sportId;

    @NotEmpty(message = "Please enter the date")
    private String date;
}
