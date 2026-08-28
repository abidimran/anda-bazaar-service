package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andabazaar.dto.appsetting.AppSettingRequestDto;
import com.andabazaar.dto.appsetting.AppSettingResponseDto;
import com.andabazaar.service.AppSettingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/app-settings")
@RequiredArgsConstructor
public class AppSettingController {

    private final AppSettingService appSettingService;

    @PostMapping
    public ResponseEntity<AppSettingResponseDto>
            createSetting(@Valid @RequestBody AppSettingRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(appSettingService.createSetting(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppSettingResponseDto>
            updateSetting(@PathVariable Long id, @Valid @RequestBody AppSettingRequestDto request) {

 return ResponseEntity.ok(appSettingService.updateSetting(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppSettingResponseDto>
            getSettingById(@PathVariable Long id) {

 return ResponseEntity.ok(appSettingService.getSettingById(id));
    }

    @GetMapping("/key/{key}")
    public ResponseEntity<AppSettingResponseDto>
            getSettingByKey(@PathVariable String key) {

 return ResponseEntity.ok(appSettingService.getSettingByKey(key));
    }

    @GetMapping
    public ResponseEntity<List<AppSettingResponseDto>>
            getAllSettings() {

 return ResponseEntity.ok(appSettingService.getAllSettings());
    }

    @GetMapping("/active")
    public ResponseEntity<List<AppSettingResponseDto>>
            getActiveSettings() {

 return ResponseEntity.ok(appSettingService.getActiveSettings());
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void>
            deactivateSetting(@PathVariable Long id) {

        appSettingService.deactivateSetting(id);

 return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
            deleteSetting(@PathVariable Long id) {

        appSettingService.deleteSetting(id);

 return ResponseEntity.noContent().build();
    }
}