package com.booking.app.responseDTO;

import com.booking.app.model.Holidays;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HolidaysListDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Holidays> holidays;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String date;
}
