package com.booking.app.responseDTO;

import com.booking.app.model.enums.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlotsDTO {
    private String timings;
    private SlotStatus status;
}
