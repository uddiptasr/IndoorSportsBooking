package com.booking.app.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SportRequestDTO {

    private int sportId;

    @NotEmpty(message = "Please enter the sportName")
    private String sportName;

    @DecimalMin(message = "Please enter the price", value = "1")
    private int price;
}
