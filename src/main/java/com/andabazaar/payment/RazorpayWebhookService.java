package com.andabazaar.payment;

import org.json.JSONObject;

import org.springframework.stereotype.Service;

import com.andabazaar.config.RazorpayConfig;
import com.razorpay.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RazorpayWebhookService {

    private final RazorpayConfig razorpayConfig;

    public boolean verifyWebhookSignature( String payload, String signature) {

        if (payload == null || payload.isBlank()) {
            return false;
        }

        if (signature == null || signature.isBlank()) {
            return false;
        }

        try {
            return Utils.verifyWebhookSignature(
                    payload,
                    signature,
                    razorpayConfig.getWebhookSecret());
        } catch (Exception e) {
            return false;
        }
    }

    public String getEvent(String payload) {

        JSONObject json =
                new JSONObject(payload);

        return json.optString("event");
    }

    public String getRazorpayOrderId( String payload) {

        JSONObject json =
                new JSONObject(payload);

        JSONObject payloadObject =
                json.optJSONObject("payload");

        if (payloadObject == null) {
            return null;
        }

        JSONObject paymentEntity =
                payloadObject
                        .optJSONObject("payment");

        if (paymentEntity != null) {
            JSONObject entity =
                    paymentEntity.optJSONObject("entity");

            if (entity != null) {
                return entity.optString("order_id",
                        null);
            }
        }

        JSONObject orderEntity =
                payloadObject
                        .optJSONObject("order");

        if (orderEntity != null) {
            JSONObject entity =
                    orderEntity.optJSONObject("entity");

            if (entity != null) {
                return entity.optString("id",
                        null);
            }
        }

        return null;
    }

    public String getRazorpayPaymentId( String payload) {

        JSONObject json =
                new JSONObject(payload);

        JSONObject payloadObject =
                json.optJSONObject("payload");

        if (payloadObject == null) {
            return null;
        }

        JSONObject paymentObject =
                payloadObject
                        .optJSONObject("payment");

        if (paymentObject == null) {
            return null;
        }

        JSONObject entity =
                paymentObject.optJSONObject("entity");

        if (entity == null) {
            return null;
        }

        return entity.optString("id",
                null);
    }
}