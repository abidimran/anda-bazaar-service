package com.andabazaar.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.andabazaar.service.CouponService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CouponExpiryScheduler {

    private final CouponService couponService;

    @Scheduled(cron = "0 0 0 * * *")
    public void expireCoupons() {

        couponService.expireCoupons();
    }
}