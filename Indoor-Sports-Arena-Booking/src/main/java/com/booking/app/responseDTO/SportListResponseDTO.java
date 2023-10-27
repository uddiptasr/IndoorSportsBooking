package com.booking.app.responseDTO;

import com.booking.app.model.Sport;
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
public class SportListResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String messsage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SportResponseDTO> sports;
}
