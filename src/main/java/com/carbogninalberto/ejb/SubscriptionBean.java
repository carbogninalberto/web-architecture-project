package com.carbogninalberto.ejb;

import com.carbogninalberto.entity.Subscription;
import com.carbogninalberto.entity.Utente;
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

    public SubscriptionBean() {
        this.status = SubscriptionStatus.INFORMATION;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public Subscription getSubscriptionByUser(Utente utente) {
        return manager.find(Subscription.class, utente);
    }

    public SubscriptionStatus setAdvanceStatus(String paymentGateway) {
        boolean useNext = false;
        for (SubscriptionStatus value : SubscriptionStatus.values()) {
            if (useNext) {
                if (this.paymentGateway == null && paymentGateway != "")
                    this.paymentGateway = paymentGateway;
                this.status = value;
                return this.status;
            }
            useNext = this.status == value;
        }
        return this.status;
    }

    public void updatePayment(Subscription subscription) {
        subscription.setPaid(true);
        manager.merge(subscription);
    }
}
