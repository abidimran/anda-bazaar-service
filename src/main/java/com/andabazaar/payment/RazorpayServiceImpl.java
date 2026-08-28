package com.andabazaar.payment;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.andabazaar.config.RazorpayConfig;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RazorpayServiceImpl implements RazorpayService {

    private final RazorpayConfig razorpayConfig;

    @Override
    public Order createOrder( BigDecimal amount, String currency, String receipt ) throws RazorpayException {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        long amountInPaise =
                amount
                        .multiply(BigDecimal.valueOf(100))
                        .longValueExact();

        RazorpayClient razorpayClient =
                new RazorpayClient(
                        razorpayConfig.getKeyId(),
                        razorpayConfig.getKeySecret());

        JSONObject orderRequest =
                new JSONObject();

        orderRequest.put("amount", amountInPaise);

        orderRequest.put("currency", currency);

        orderRequest.put("receipt", receipt);

        orderRequest.put("payment_capture", 1);

        return razorpayClient.orders.create(
                orderRequest);
    }

    @Override
    public boolean verifySignature( String orderId, String paymentId, String signature ) throws RazorpayException {

        if (orderId == null ||
                paymentId == null ||
                signature == null) {

            return false;
        }

        JSONObject attributes =
                new JSONObject();

        attributes.put("razorpay_order_id", orderId);

        attributes.put("razorpay_payment_id", paymentId);

        attributes.put("razorpay_signature", signature);

        return Utils.verifyPaymentSignature(
                attributes,
                razorpayConfig.getKeySecret());
    }
}