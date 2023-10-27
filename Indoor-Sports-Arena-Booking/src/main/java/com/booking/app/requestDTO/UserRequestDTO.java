package com.booking.app.requestDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {



    private int userId;

    @NotEmpty(message = "Please enter the firstName")
    private String firstName;
    @NotEmpty(message = "Please enter the lastName")
    private String lastName;
    @NotEmpty(message = "Please enter the password")
    private String password;



}
