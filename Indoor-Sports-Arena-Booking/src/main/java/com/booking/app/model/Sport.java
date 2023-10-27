package com.booking.app.model;

import com.booking.app.requestDTO.SportRequestDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.context.annotation.PropertySource;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int sportId;


    @Column(nullable = false,unique=true)
    @JsonDeserialize(using = ToLowerCaseDeserializer.class)
    private String sportName;

    @Column(nullable = false)
    private int price;

    public Sport(SportRequestDTO sportRequestDTO){
        this.setPrice(sportRequestDTO.getPrice());
        this.setSportId(sportRequestDTO.getSportId());
        this.setSportName(sportRequestDTO.getSportName());
    }
    @Override
    public String toString() {
        return "Sport{" +
                "sportId=" + sportId +
                ", sport_name='" + sportName + '\'' +
                ", price=" + price +
                '}';
    }

}
