package com.andabazaar.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.location.StateRequestDto;
import com.andabazaar.dto.location.StateResponseDto;
import com.andabazaar.entity.State;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.StateRepository;
import com.andabazaar.service.StateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StateServiceImpl implements StateService {

    private final StateRepository stateRepository;

    @Override
    public StateResponseDto createState( StateRequestDto request) {

        if (stateRepository.existsByNameIgnoreCase(
                request.getName())) {

            throw new BadRequestException("State already exists");
        }

        State state = State.builder()
                .name(request.getName().trim())
                .active(true)
                .build();

        return mapToResponse(
                stateRepository.save(state));
    }

    @Override
    @Transactional(readOnly = true)
    public StateResponseDto getStateById(Long id) {

        return mapToResponse(findState(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDto> getAllStates() {

        return stateRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StateResponseDto> getActiveStates() {

        return stateRepository
                .findByActiveTrueOrderByNameAsc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public StateResponseDto updateState( Long id, StateRequestDto request) {

        State state = findState(id);

        if (!state.getName()
                .equalsIgnoreCase(request.getName())
                && stateRepository.existsByNameIgnoreCase( request.getName())) {

            throw new BadRequestException("State already exists");
        }

        state.setName(request.getName().trim());

        return mapToResponse(
                stateRepository.save(state));
    }

    @Override
    public void deleteState(Long id) {

        State state = findState(id);

        // Soft delete
        state.setActive(false);

        stateRepository.save(state);
    }

    private State findState(Long id) {

        return stateRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("State not found with id: " + id));
    }

    private StateResponseDto mapToResponse( State state) {

        return StateResponseDto.builder()
                .id(state.getId())
                .name(state.getName())
                .active(state.getActive())
                .build();
    }
}