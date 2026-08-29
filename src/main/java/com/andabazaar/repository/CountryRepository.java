package com.andabazaar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.repository.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
