package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andabazaar.dto.appsetting.AppSettingRequestDto;
import com.andabazaar.dto.appsetting.AppSettingResponseDto;
import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.service.AppSettingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "App Settings", description = "Application settings management")
@RestController
@RequestMapping("/api/app-settings")
@RequiredArgsConstructor
public class AppSettingController {

    private final AppSettingService appSettingService;

    @Operation(summary = "Create Setting")
    @PostMapping
    public ResponseEntity<AppSettingResponseDto>
            createSetting(@Valid @RequestBody AppSettingRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(appSettingService.createSetting(request));
    }

    @Operation(summary = "Update Setting")
    @PutMapping("/{id}")
    public ResponseEntity<AppSettingResponseDto>
            updateSetting(@PathVariable Long id, @Valid @RequestBody AppSettingRequestDto request) {

 return ResponseEntity.ok(appSettingService.updateSetting(id, request));
    }

    @Operation(summary = "Get Setting By Id")
    @GetMapping("/{id}")
    public ResponseEntity<AppSettingResponseDto>
            getSettingById(@PathVariable Long id) {

 return ResponseEntity.ok(appSettingService.getSettingById(id));
    }

    @Operation(summary = "Get Setting By Key")
    @GetMapping("/key/{key}")
    public ResponseEntity<AppSettingResponseDto>
            getSettingByKey(@PathVariable String key) {

 return ResponseEntity.ok(appSettingService.getSettingByKey(key));
    }

    @Operation(summary = "Get All Settings")
    @GetMapping
    public ResponseEntity<PagedResponse<AppSettingResponseDto>>
            getAllSettings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(appSettingService.getAllSettings(), page, size));
    }

    @Operation(summary = "Get Active Settings")
    @GetMapping("/active")
    public ResponseEntity<PagedResponse<AppSettingResponseDto>>
            getActiveSettings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(appSettingService.getActiveSettings(), page, size));
    }

    @Operation(summary = "Deactivate Setting")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void>
            deactivateSetting(@PathVariable Long id) {

        appSettingService.deactivateSetting(id);

 return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete Setting")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
            deleteSetting(@PathVariable Long id) {

        appSettingService.deleteSetting(id);

 return ResponseEntity.noContent().build();
    }
}
