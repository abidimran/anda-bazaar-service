package com.andabazaar.serviceimpl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.entity.Coupon;
import com.andabazaar.entity.CouponUsage;
import com.andabazaar.entity.User;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.CouponRepository;
import com.andabazaar.repository.CouponUsageRepository;
import com.andabazaar.repository.UserRepository;
import com.andabazaar.service.CouponUsageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponUsageServiceImpl implements CouponUsageService {

    private final CouponUsageRepository couponUsageRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    // =========================================================
    // RECORD COUPON USAGE
    // =========================================================

    @Override
    public CouponUsage recordUsage(
            Long couponId,
            Long userId) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Coupon not found with id: "
                                        + couponId
                        )
                );

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: "
                                        + userId
                        )
                );

        if (coupon.getUsedCount()
                >= coupon.getUsageLimit()) {

            throw new BadRequestException(
                    "Coupon usage limit reached"
            );
        }

        if (couponUsageRepository
                .existsByCouponIdAndUserId(
                        couponId,
                        userId
                )) {

            throw new BadRequestException(
                    "User has already used this coupon"
            );
        }

        CouponUsage usage = CouponUsage.builder()
                .coupon(coupon)
                .user(user)
                .discountAmount(BigDecimal.ZERO)
                .originalAmount(BigDecimal.ZERO)
                .finalAmount(BigDecimal.ZERO)
                .build();

        coupon.setUsedCount(
                coupon.getUsedCount() + 1
        );

        couponRepository.save(coupon);

        return couponUsageRepository.save(usage);
    }

    // =========================================================
    // GET USAGE BY ID
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public CouponUsage getUsageById(Long id) {

        return couponUsageRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Coupon usage not found with id: "
                                        + id
                        )
                );
    }

    // =========================================================
    // GET ALL USAGE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<CouponUsage> getAllUsage() {

        return couponUsageRepository.findAll();
    }

    // =========================================================
    // GET USER USAGE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<CouponUsage> getUserUsage(
            Long userId) {

        if (!userRepository.existsById(userId)) {

            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return couponUsageRepository
                .findByUserIdOrderByUsedAtDesc(userId);
    }

    // =========================================================
    // GET COUPON USAGE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<CouponUsage> getCouponUsage(
            Long couponId) {

        if (!couponRepository.existsById(couponId)) {

            throw new ResourceNotFoundException(
                    "Coupon not found with id: "
                            + couponId
            );
        }

        return couponUsageRepository
                .findByCouponIdOrderByUsedAtDesc(
                        couponId
                );
    }

    // =========================================================
    // GET USER + COUPON USAGE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<CouponUsage> getUserCouponUsage(
            Long userId,
            Long couponId) {

        return couponUsageRepository
                .findByUserIdAndCouponIdOrderByUsedAtDesc(
                        userId,
                        couponId
                );
    }

    // =========================================================
    // COUNT USER USAGE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public long countUserUsage(
            Long userId) {

        return couponUsageRepository
                .countByUserId(userId);
    }

    // =========================================================
    // COUNT COUPON USAGE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public long countCouponUsage(
            Long couponId) {

        return couponUsageRepository
                .countByCouponId(couponId);
    }
}