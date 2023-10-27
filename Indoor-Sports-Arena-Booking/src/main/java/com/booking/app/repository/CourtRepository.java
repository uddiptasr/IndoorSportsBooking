package com.booking.app.repository;

import com.booking.app.model.Court;
import com.booking.app.model.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourtRepository extends JpaRepository<Court,Integer> {

    List<Court> findBySportSportId(int sportId);
    Optional<Court>  findBySportAndCourtNo(Sport sport, int courtNo);
    Boolean existsBySportSportIdAndCourtNo(int sportId,int courtNo);

    Court findCourtNoByCourtId(int courtId);
    List<Court> findCourtIdBySportSportId(int sportId);
    Optional<Court>  findByCourtNoAndSportSportId(int courtNo,int sportId);


}
