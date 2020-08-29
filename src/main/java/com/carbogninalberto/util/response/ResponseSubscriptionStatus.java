package com.carbogninalberto.util.response;

import java.io.Serializable;

public class ResponseSubscriptionStatus extends Response implements Serializable {

    String subscriptionStatus;
    String paymentGateway;

    public ResponseSubscriptionStatus(String msg, String subscriptionStatus, String paymentGateway) {
        super(msg);
        this.subscriptionStatus = subscriptionStatus;
        this.paymentGateway = paymentGateway;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
}
