package com.carbogninalberto.ejb;

import com.carbogninalberto.itf.Logging;
import com.carbogninalberto.util.SubscriptionStatus;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Stateful
@LocalBean
public class SubscriptionBean implements Serializable, Logging {

    @PersistenceContext(name = "persistenceJPA")
    EntityManager manager;
    String paymentGateway;
    SubscriptionStatus status;


    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }
}
