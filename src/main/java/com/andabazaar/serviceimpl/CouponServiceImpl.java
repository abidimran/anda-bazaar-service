package com.andabazaar.serviceimpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andabazaar.dto.coupon.CouponRequestDto;
import com.andabazaar.dto.coupon.CouponResponseDto;
import com.andabazaar.entity.Coupon;
import com.andabazaar.enums.CouponStatus;
import com.andabazaar.exception.BadRequestException;
import com.andabazaar.exception.ResourceNotFoundException;
import com.andabazaar.repository.CouponRepository;
import com.andabazaar.service.CouponService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    public CouponResponseDto createCoupon( CouponRequestDto request) {

        validateDates( request.getStartDate(), request.getEndDate());

        if (couponRepository.existsByCodeIgnoreCase(
                request.getCode())) {

            throw new BadRequestException("Coupon code already exists");
        }

        validateDiscount(request);

        Coupon coupon = Coupon.builder()
                .code( request.getCode()
                                .trim()
                                .toUpperCase()
                )
                .description( request.getDescription()
                )
                .discountAmount( request.getDiscountAmount()
                )
                .percentage( request.getPercentage()
                )
                .minimumOrderAmount( request.getMinimumOrderAmount()
                )
                .usageLimit( request.getUsageLimit()
                )
                .usedCount(0)
                .startDate( request.getStartDate()
                )
                .endDate( request.getEndDate()
                )
                .status( CouponStatus.ACTIVE )
                .active(true)
                .build();

        return mapToResponse(
                couponRepository.save(coupon));
    }

    @Override
    public CouponResponseDto updateCoupon( Long id, CouponRequestDto request) {

        Coupon coupon = findCoupon(id);

        validateDates( request.getStartDate(), request.getEndDate());

        checkDuplicateCode( coupon, request.getCode());

        validateDiscount(request);

        coupon.setCode( request.getCode() .trim() .toUpperCase());

        coupon.setDescription( request.getDescription());

        coupon.setDiscountAmount( request.getDiscountAmount());

        coupon.setPercentage( request.getPercentage());

        coupon.setMinimumOrderAmount( request.getMinimumOrderAmount());

        coupon.setUsageLimit( request.getUsageLimit());

        coupon.setStartDate( request.getStartDate());

        coupon.setEndDate( request.getEndDate());

        return mapToResponse(
                couponRepository.save(coupon));
    }

    @Override
    @Transactional(readOnly = true)
    public CouponResponseDto getCouponById( Long id) {

        return mapToResponse(
                findCoupon(id));
    }

    @Override
    @Transactional(readOnly = true)
    public CouponResponseDto getCouponByCode( String code) {

        Coupon coupon = couponRepository
                .findByCodeIgnoreCase(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coupon not found with code: "
                                        + code
                        ));

        return mapToResponse(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponseDto> getActiveCoupons() {

        LocalDateTime now = LocalDateTime.now();

        return couponRepository
                .findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .filter(coupon ->
                        coupon.getStatus()
                                == CouponStatus.ACTIVE
                        && !coupon.getStartDate()
                                .isAfter(now)
                        && !coupon.getEndDate()
                                .isBefore(now)
                        && coupon.getUsedCount()
                                < coupon.getUsageLimit()
                )
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponseDto> getAllCoupons() {

        return couponRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deactivateCoupon(Long id) {

        Coupon coupon = findCoupon(id);

        coupon.setActive(false);

        coupon.setStatus( CouponStatus.INACTIVE);

        couponRepository.save(coupon);
    }

    @Override
    public void deleteCoupon(Long id) {

        Coupon coupon = findCoupon(id);

        couponRepository.delete(coupon);
    }

    @Override
    public CouponResponseDto applyCoupon( String code, BigDecimal orderAmount) {

        Coupon coupon = couponRepository
                .findByCodeIgnoreCase(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coupon not found"));

        LocalDateTime now = LocalDateTime.now();

        if (!Boolean.TRUE.equals(
                coupon.getActive())) {

            throw new BadRequestException("Coupon is inactive");
        }

        if (coupon.getStatus()
                != CouponStatus.ACTIVE) {

            throw new BadRequestException("Coupon is not active");
        }

        if (now.isBefore(
                coupon.getStartDate())) {

            throw new BadRequestException("Coupon is not active yet");
        }

        if (now.isAfter(
                coupon.getEndDate())) {

            throw new BadRequestException("Coupon has expired");
        }

        if (coupon.getUsedCount()
                >= coupon.getUsageLimit()) {

            throw new BadRequestException("Coupon usage limit reached");
        }

        if (orderAmount.compareTo(
                coupon.getMinimumOrderAmount()) < 0) {

            throw new BadRequestException("Minimum order amount is "
                            + coupon.getMinimumOrderAmount());
        }

        return mapToResponse(coupon);
    }

    @Override
    public void expireCoupons() {

        LocalDateTime now = LocalDateTime.now();

        List<Coupon> coupons =
                couponRepository
                        .findByStatusAndEndDateBefore( CouponStatus.ACTIVE, now);

        for (Coupon coupon : coupons) {

            coupon.setStatus( CouponStatus.EXPIRED);

            coupon.setActive(false);
        }

        couponRepository.saveAll(coupons);
    }

    private Coupon findCoupon(Long id) {

        return couponRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Coupon not found with id: "
                                        + id
                        ));
    }

    private void validateDates( LocalDateTime startDate, LocalDateTime endDate) {

        if (endDate.isBefore(startDate)) {

            throw new BadRequestException("End date cannot be before start date");
        }
    }

    private void validateDiscount( CouponRequestDto request) {

        if (Boolean.TRUE.equals(
                request.getPercentage())) {

            if (request.getDiscountAmount()
                    .compareTo( BigDecimal.valueOf(100)
                    ) > 0) {

                throw new BadRequestException("Percentage discount cannot exceed 100");
            }
        }
    }

    private void checkDuplicateCode( Coupon coupon, String newCode) {

        if (!coupon.getCode()
                .equalsIgnoreCase( newCode.trim()
                )) {

            if (couponRepository
                    .existsByCodeIgnoreCase( newCode )) {

                throw new BadRequestException("Coupon code already exists");
            }
        }
    }

    private CouponResponseDto mapToResponse( Coupon coupon) {

        LocalDateTime now =
                LocalDateTime.now();

        boolean expired =
                coupon.getEndDate()
                        .isBefore(now);

        boolean usable =
                Boolean.TRUE.equals( coupon.getActive() )
                && coupon.getStatus()
                        == CouponStatus.ACTIVE
                && !now.isBefore( coupon.getStartDate() )
                && !expired
                && coupon.getUsedCount()
                        < coupon.getUsageLimit();

        return CouponResponseDto.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .description( coupon.getDescription()
                )
                .discountAmount( coupon.getDiscountAmount()
                )
                .percentage( coupon.getPercentage()
                )
                .minimumOrderAmount( coupon.getMinimumOrderAmount()
                )
                .usageLimit( coupon.getUsageLimit()
                )
                .usedCount( coupon.getUsedCount()
                )
                .startDate( coupon.getStartDate()
                )
                .endDate( coupon.getEndDate()
                )
                .status( coupon.getStatus()
                )
                .active( coupon.getActive()
                )
                .expired(expired)
                .usable(usable)
                .createdAt( coupon.getCreatedAt()
                )
                .updatedAt( coupon.getUpdatedAt()
                )
                .build();
    }
}