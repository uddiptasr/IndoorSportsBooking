package com.booking.app.model;

import com.booking.app.requestDTO.UserRequestDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    //@JsonDeserialize(using = ToLowerCaseDeserializer.class)
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String password;

    //@JsonDeserialize(using = ToLowerCaseDeserializer.class)
    @Column(nullable = false)
    private String lastName;
    @Column(updatable = false)

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;


    public User(UserRequestDTO userRequestDTO) {
        this.setUserId(userRequestDTO.getUserId());
        this.setFirstName(userRequestDTO.getFirstName());
        this.setLastName(userRequestDTO.getLastName());
        this.setPassword(userRequestDTO.getPassword());
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", password='" + password + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
