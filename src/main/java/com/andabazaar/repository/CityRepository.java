package com.andabazaar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.repository.entity.City;

public interface CityRepository
        extends JpaRepository<City, Long> {

    List<City> findByStateIdAndActiveTrueOrderByNameAsc( Long stateId);

    Optional<City> findByNameIgnoreCaseAndStateId( String name, Long stateId);

    boolean existsByNameIgnoreCaseAndStateId( String name, Long stateId);
}