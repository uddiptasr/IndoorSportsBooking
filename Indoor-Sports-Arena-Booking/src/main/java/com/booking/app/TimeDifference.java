package com.booking.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class TimeDifference {
    private long difference_In_Years;
    private long difference_In_Days;
    private long difference_In_Hours;
    private long difference_In_Minutes;
    private long difference_In_Seconds;

}
