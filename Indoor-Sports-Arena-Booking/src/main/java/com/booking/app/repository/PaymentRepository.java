package com.booking.app.repository;

import com.booking.app.model.Booking;
import com.booking.app.model.CourtSlots;
import com.booking.app.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    //Payment findByCourtslotsCourtslotId(int courtslotId);
    //Payment findByBookingCourtSlotsCourtslotId(int courtslotId);


}
