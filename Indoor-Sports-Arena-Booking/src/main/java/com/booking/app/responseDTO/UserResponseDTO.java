package com.booking.app.responseDTO;

import com.booking.app.model.User;
import com.booking.app.requestDTO.UserRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)//to not include if null
    private String messsage;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int userId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String firstName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;//camelcase only
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastModifiedDate;

    public UserResponseDTO(UserRequestDTO userRequestDTO){
        this.setUserId(userRequestDTO.getUserId());
        this.setFirstName(userRequestDTO.getFirstName());
        this.setLastName(userRequestDTO.getLastName());

    }



}
