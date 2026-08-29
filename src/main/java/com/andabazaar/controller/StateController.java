package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.dto.location.StateRequestDto;
import com.andabazaar.dto.location.StateResponseDto;
import com.andabazaar.service.StateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "States", description = "State management")
@RestController
@RequestMapping("/api/states")
@RequiredArgsConstructor
public class StateController {

    private final StateService stateService;

    @Operation(summary = "Create State")
    @PostMapping
    public ResponseEntity<StateResponseDto> createState(@Valid @RequestBody StateRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(stateService.createState(request));
    }

    @Operation(summary = "Get All States")
    @GetMapping
    public ResponseEntity<PagedResponse<StateResponseDto>>
            getAllStates(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(stateService.getAllStates(), page, size));
    }

    @Operation(summary = "Get Active States")
    @GetMapping("/active")
    public ResponseEntity<PagedResponse<StateResponseDto>>
            getActiveStates(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(stateService.getActiveStates(), page, size));
    }

    @Operation(summary = "Get State")
    @GetMapping("/{id}")
    public ResponseEntity<StateResponseDto> getState(@PathVariable Long id) {

 return ResponseEntity.ok(stateService.getStateById(id));
    }

    @Operation(summary = "Update State")
    @PutMapping("/{id}")
    public ResponseEntity<StateResponseDto> updateState(@PathVariable Long id, @Valid @RequestBody StateRequestDto request) {

 return ResponseEntity.ok(stateService.updateState(id, request));
    }

    @Operation(summary = "Delete State")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable Long id) {

        stateService.deleteState(id);

 return ResponseEntity.noContent().build();
    }
}
