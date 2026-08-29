package com.andabazaar.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andabazaar.dto.common.PagedResponse;
import com.andabazaar.dto.subscription.SubscribeRequestDto;
import com.andabazaar.dto.subscription.SubscriptionPlanRequestDto;
import com.andabazaar.dto.subscription.SubscriptionPlanResponseDto;
import com.andabazaar.dto.subscription.SubscriptionResponseDto;
import com.andabazaar.service.SubscriptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Subscriptions", description = "Subscription plans and user subscriptions")
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Operation(summary = "Get Active Plans")
    @GetMapping("/plans")
    public ResponseEntity<PagedResponse<SubscriptionPlanResponseDto>>
            getActivePlans(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(subscriptionService.getActivePlans(), page, size));
    }

    @Operation(summary = "Get Plan")
    @GetMapping("/plans/{id}")
    public ResponseEntity<SubscriptionPlanResponseDto>
            getPlan(@PathVariable Long id) {

 return ResponseEntity.ok(subscriptionService.getPlanById(id));
    }

    @Operation(summary = "Subscribe")
    @PostMapping("/user/{userId}")
    public ResponseEntity<SubscriptionResponseDto>
            subscribe(@PathVariable Long userId, @Valid @RequestBody SubscribeRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.subscribe(userId, request));
    }

    @Operation(summary = "Get Current Subscription")
    @GetMapping("/user/{userId}/current")
    public ResponseEntity<SubscriptionResponseDto>
            getCurrentSubscription(@PathVariable Long userId) {

 return ResponseEntity.ok(subscriptionService.getCurrentSubscription(userId));
    }

    @Operation(summary = "Get Subscription History")
    @GetMapping("/user/{userId}/history")
    public ResponseEntity<PagedResponse<SubscriptionResponseDto>>
            getSubscriptionHistory(@PathVariable Long userId,
                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

 return ResponseEntity.ok(PagedResponse.fromList(subscriptionService.getSubscriptionHistory(userId), page, size));
    }

    @Operation(summary = "Create Plan")
    @PostMapping("/admin/plans")
    public ResponseEntity<SubscriptionPlanResponseDto>
            createPlan(@Valid @RequestBody SubscriptionPlanRequestDto request) {

 return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.createPlan(request));
    }

    @Operation(summary = "Update Plan")
    @PutMapping("/admin/plans/{id}")
    public ResponseEntity<SubscriptionPlanResponseDto>
            updatePlan(@PathVariable Long id, @Valid @RequestBody SubscriptionPlanRequestDto request) {

 return ResponseEntity.ok(subscriptionService.updatePlan(id, request));
    }

    @Operation(summary = "Deactivate Plan")
    @DeleteMapping("/admin/plans/{id}")
    public ResponseEntity<Void>
            deactivatePlan(@PathVariable Long id) {

        subscriptionService.deactivatePlan(id);

 return ResponseEntity.noContent().build();
    }
}
