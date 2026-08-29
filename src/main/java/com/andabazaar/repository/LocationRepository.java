package com.andabazaar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.repository.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByCityId(Long cityId);

    List<Location> findByStateId(Long stateId);

    List<Location> findByCountryId(Long countryId);

    List<Location> findByRapidEnabledTrue();

    Optional<Location> findByCityIdAndStateId(Long cityId, Long stateId);
}
