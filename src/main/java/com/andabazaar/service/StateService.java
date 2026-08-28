package com.andabazaar.service;

import java.util.List;

import com.andabazaar.dto.location.StateRequestDto;
import com.andabazaar.dto.location.StateResponseDto;

public interface StateService {

    StateResponseDto createState(StateRequestDto request);

    StateResponseDto getStateById(Long id);

    List<StateResponseDto> getAllStates();

    List<StateResponseDto> getActiveStates();

    StateResponseDto updateState(Long id, StateRequestDto request);

    void deleteState(Long id);
}