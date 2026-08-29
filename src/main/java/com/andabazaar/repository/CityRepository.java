package com.andabazaar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.repository.entity.City;

public interface CityRepository
        extends JpaRepository<City, Long> {

    Optional<City> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<City> findAllByOrderByNameAsc();
}
