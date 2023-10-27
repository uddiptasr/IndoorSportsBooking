package com.booking.app.model;

import com.booking.app.exception.ResourceNotFoundException;
import com.booking.app.repository.SportRepository;
import com.booking.app.requestDTO.CourtRequestDTO;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int courtId;

    @ManyToOne
    @JoinColumn(name = "sportId", referencedColumnName = "sportId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonUnwrapped
    private Sport sport;

    @Column(nullable = false)
    private int courtNo;




    @Override
    public String toString() {
        return "Court{" +
                "courtId=" + courtId +
                ", sport=" + sport +
                ", courtNo=" + courtNo +
                '}';
    }

}
