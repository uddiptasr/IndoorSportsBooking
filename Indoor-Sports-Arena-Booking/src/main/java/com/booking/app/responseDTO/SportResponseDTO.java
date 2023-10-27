package com.booking.app.responseDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SportResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String messsage;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int sportId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sportName;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int price;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> courts;

}
