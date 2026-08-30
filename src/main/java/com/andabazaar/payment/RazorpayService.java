package com.andabazaar.payment;

import java.math.BigDecimal;

import com.razorpay.Order;
import com.razorpay.RazorpayException;

public interface RazorpayService {
    Order createOrder(BigDecimal amount, String currency, String receipt ) throws RazorpayException;

    boolean verifySignature(String orderId, String paymentId, String signature ) throws RazorpayException;
}
