package com.booking.app.repository;

import com.booking.app.model.Booking;
import com.booking.app.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking,Integer> {
   List<Booking> findByUserUserId(int userId);
   List<Booking> getByUserUserId(int userId);
   Optional<Booking> findByBookingReferenceNo(String bookingReferenceNo);
   Booking findByCourtSlotsCourtslotId(int courtSlotsId);

   Booking getByCourtSlotsCourtslotId(int courtSlotsId);
   Booking getByPaymentPaymentId(UUID paymentId);
}
