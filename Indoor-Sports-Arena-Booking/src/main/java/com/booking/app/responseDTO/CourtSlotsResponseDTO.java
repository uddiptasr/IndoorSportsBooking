package com.booking.app.responseDTO;

import com.booking.app.model.enums.SlotStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtSlotsResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SlotStatus slotStatus;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int courtSlotId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int courtId;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int courtNumber;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int sportId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sportName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String startTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String endTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String date;


}
