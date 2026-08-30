package com.andabazaar.repository;

import com.andabazaar.repository.entity.State;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository
        extends JpaRepository<State, Long> {
    Optional<State> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<State> findAllByOrderByNameAsc();
}
