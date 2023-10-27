package com.booking.app.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Holidays {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int holidayId;

    @Column(nullable = false)
    private String date;

    @Override
    public String toString() {
        return "Holidays{" +
                "holidayId=" + holidayId +
                ", date='" + date + '\'' +
                '}';
    }

}
