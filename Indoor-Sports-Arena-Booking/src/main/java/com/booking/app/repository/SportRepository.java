package com.booking.app.repository;

import com.booking.app.model.Sport;
import com.booking.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportRepository extends JpaRepository<Sport,Integer> {
    Sport getBySportId(int sportId);
}
