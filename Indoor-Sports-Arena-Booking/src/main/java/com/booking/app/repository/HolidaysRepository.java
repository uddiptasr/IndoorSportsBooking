package com.booking.app.repository;

import com.booking.app.model.Holidays;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HolidaysRepository extends JpaRepository<Holidays,Integer> {

    boolean existsByDate(String date);
    Optional<Holidays> findByDate(String date);

    Holidays deleteByDate(String date);
    Integer removeByDate(String date);

}
