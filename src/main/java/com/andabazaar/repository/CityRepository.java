package com.andabazaar.repository;

import com.andabazaar.repository.entity.City;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository
        extends JpaRepository<City, Long> {
    Optional<City> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<City> findAllByOrderByNameAsc();
}
