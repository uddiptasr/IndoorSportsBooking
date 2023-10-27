package com.booking.app.requestDTO;

import com.booking.app.model.Sport;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtRequestDTO {
    private int courtId;
    @DecimalMin(message = "Please enter the sportId", value = "1")
    private int sportId;
    @DecimalMin(message = "Please enter the courtNo", value = "1")
    private int courtNo;
}
