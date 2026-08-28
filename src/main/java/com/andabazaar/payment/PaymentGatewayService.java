package com.andabazaar.payment;

public interface PaymentGatewayService {

    boolean verifyPurchase(
            String packageName,
            String productId,
            String purchaseToken);

}