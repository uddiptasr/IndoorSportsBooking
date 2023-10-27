package com.booking.app.responseDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtSlotListResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private List<CourtSlotsResponseDTO> courstSlots;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private List<String> slots;
}
