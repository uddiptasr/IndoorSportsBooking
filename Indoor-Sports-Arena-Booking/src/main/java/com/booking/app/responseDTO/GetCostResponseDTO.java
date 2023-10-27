package com.booking.app.responseDTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCostResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int totalCost;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int costPerHalfHour;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sport;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String startTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String endTime;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String duration;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String date;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

}
