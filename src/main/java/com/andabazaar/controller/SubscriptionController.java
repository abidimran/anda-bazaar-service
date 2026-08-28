package com.andabazaar.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.subscription.SubscribeRequestDto;
import com.andabazaar.dto.subscription.SubscriptionPlanRequestDto;
import com.andabazaar.dto.subscription.SubscriptionPlanResponseDto;
import com.andabazaar.dto.subscription.SubscriptionResponseDto;
import com.andabazaar.service.SubscriptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // =========================
    // PUBLIC / USER
    // =========================

    @GetMapping("/plans")
    public ResponseEntity<
            List<SubscriptionPlanResponseDto>>
            getActivePlans() {

        return ResponseEntity.ok(
                subscriptionService.getActivePlans()
        );
    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<
            SubscriptionPlanResponseDto>
            getPlan(@PathVariable Long id) {

        return ResponseEntity.ok(
                subscriptionService.getPlanById(id)
        );
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<SubscriptionResponseDto>
            subscribe(
                    @PathVariable Long userId,
                    @Valid @RequestBody
                    SubscribeRequestDto request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    subscriptionService.subscribe(
                            userId,
                            request
                    )
                );
    }

    @GetMapping("/user/{userId}/current")
    public ResponseEntity<SubscriptionResponseDto>
            getCurrentSubscription(
                    @PathVariable Long userId) {

        return ResponseEntity.ok(
                subscriptionService
                    .getCurrentSubscription(userId)
        );
    }

    @GetMapping("/user/{userId}/history")
    public ResponseEntity<
            List<SubscriptionResponseDto>>
            getSubscriptionHistory(
                    @PathVariable Long userId) {

        return ResponseEntity.ok(
                subscriptionService
                    .getSubscriptionHistory(userId)
        );
    }

    // =========================
    // ADMIN
    // =========================

    @PostMapping("/admin/plans")
    public ResponseEntity<
            SubscriptionPlanResponseDto>
            createPlan(
                    @Valid @RequestBody
                    SubscriptionPlanRequestDto request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    subscriptionService.createPlan(
                            request
                    )
                );
    }

    @PutMapping("/admin/plans/{id}")
    public ResponseEntity<
            SubscriptionPlanResponseDto>
            updatePlan(
                    @PathVariable Long id,
                    @Valid @RequestBody
                    SubscriptionPlanRequestDto request) {

        return ResponseEntity.ok(
                subscriptionService.updatePlan(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/admin/plans/{id}")
    public ResponseEntity<Void>
            deactivatePlan(
                    @PathVariable Long id) {

        subscriptionService.deactivatePlan(id);

        return ResponseEntity.noContent().build();
    }
}