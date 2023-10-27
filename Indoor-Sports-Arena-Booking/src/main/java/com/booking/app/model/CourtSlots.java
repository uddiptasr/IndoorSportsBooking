package com.booking.app.model;

import com.booking.app.model.enums.SlotStatus;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CourtSlots {

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private int courtslotId;

    @ManyToOne
    @JsonUnwrapped
    @JoinColumn(name = "courtId", referencedColumnName = "courtId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Court court;

    @ManyToOne
    @JsonUnwrapped
    @JoinColumn(name = "sportId", referencedColumnName = "sportId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Sport sport;

    @Column(nullable = false)
    private String StartTime;

    @Column(nullable = false)
    private String EndTime;

    @Column(nullable = false)
    private String date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus slotStatus;



    @Override
    public String toString() {
        return "CourtSlots{" +
                "courtslotId=" + courtslotId +
                ", court=" + court +
                ", sport=" + sport +
                ", StartTime='" + StartTime + '\'' +
                ", EndTime='" + EndTime + '\'' +
                ", date='" + date + '\'' +
                '}';
    }



}
