package com.booknest.availabilityservice.repository;

import com.booknest.availabilityservice.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    List<Availability> findByIsbnIn(List<String> isbns);
}
