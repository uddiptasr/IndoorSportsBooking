package com.booking.app.model;


import com.booking.app.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
//import org.joda.time.DateTime;

import javax.persistence.*;
import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bookingId;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToOne
    @JoinColumn(name = "PaymentId", referencedColumnName = "PaymentId", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Payment payment;

    @OneToOne
    @JoinColumn(name = "courtslotId", referencedColumnName = "courtslotId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CourtSlots courtSlots;

    private int totalCost;

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp createDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;

    @Column(unique=true)
    private String bookingReferenceNo;

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", user=" + user +
                ", payment=" + payment +
                ", courtSlots=" + courtSlots +
                ", totalCost=" + totalCost +
                ", createDate=" + createDate +
                ", bookingStatus='" + bookingStatus + '\'' +
                ", bookingReferenceNo='" + bookingReferenceNo + '\'' +
                '}';
    }
/*
    @ManyToOne
    @JoinColumn(name = "cid", referencedColumnName = "courtId", nullable = false)
    private Court court;

    @ManyToOne
    @JoinColumn(name = "sid", referencedColumnName = "sportId", nullable = false)
    private Sport sport;

    @ManyToOne
    @JoinColumn(name="date",referencedColumnName = "date")
    private CourtSlots courtSlots;

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", user=" + user +
                ", court=" + court +
                ", sport=" + sport +
                ", courtSlots=" + courtSlots +
                '}';
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public CourtSlots getCourtSlots() {
        return courtSlots;
    }

    public void setCourtSlots(CourtSlots courtSlots) {
        this.courtSlots = courtSlots;
    }

    public Booking(int bookingId, User user, Court court, Sport sport, CourtSlots courtSlots) {
        this.bookingId = bookingId;
        this.user = user;
        this.court = court;
        this.sport = sport;
        this.courtSlots = courtSlots;
    }*/



}
