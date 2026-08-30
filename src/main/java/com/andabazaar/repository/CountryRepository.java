package com.andabazaar.repository;

import com.andabazaar.repository.entity.Country;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByNameIgnoreCase(String name);
}
