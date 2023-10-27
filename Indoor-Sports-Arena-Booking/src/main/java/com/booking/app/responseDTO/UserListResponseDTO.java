package com.booking.app.responseDTO;

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
public class UserListResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UserResponseDTO> userList;
}
