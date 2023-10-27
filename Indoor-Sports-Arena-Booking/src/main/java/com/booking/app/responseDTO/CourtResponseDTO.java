package com.booking.app.responseDTO;

import com.booking.app.model.Court;
import com.booking.app.model.Sport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtResponseDTO {

    private int sportId;
    private String sportName;
    private int price;
    private int courtNo;
    public CourtResponseDTO(Court court){
        this.setSportId(court.getSport().getSportId());
        this.setCourtNo(court.getCourtNo());
        this.setPrice(court.getSport().getPrice());
        this.setSportName(court.getSport().getSportName());

    }

}
