package com.andabazaar.repository;

import com.andabazaar.repository.entity.Location;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByCityId(Long cityId);

    List<Location> findByRapidEnabledTrue();
}
