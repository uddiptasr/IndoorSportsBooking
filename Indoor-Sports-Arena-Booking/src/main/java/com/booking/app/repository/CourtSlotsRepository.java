package com.booking.app.repository;

import com.booking.app.model.Court;
import com.booking.app.model.CourtSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CourtSlotsRepository extends JpaRepository<CourtSlots,Integer> {
    List<CourtSlots> findBySportSportIdAndDateAndCourtCourtId(int sportId, String Date, int courtId);
    List<CourtSlots> findByDate(String Date);

    List<CourtSlots> findCourtCourtIdDistinctBySportSportId(int sportId);


}
