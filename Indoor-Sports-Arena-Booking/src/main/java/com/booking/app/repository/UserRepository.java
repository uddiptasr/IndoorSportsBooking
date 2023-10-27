package com.booking.app.repository;

import com.booking.app.model.User;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User getByUserId(int userId);
    Boolean existsByUserId(int userId);
}
