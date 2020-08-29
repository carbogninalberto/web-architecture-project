package com.carbogninalberto.ejb;

import com.carbogninalberto.entity.Subscription;
import com.carbogninalberto.entity.Utente;
import com.carbogninalberto.itf.Logging;
import com.carbogninalberto.util.response.SubscriptionStatus;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

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

    /**
     * List all subscriptions.
     * @return
     */
    public List<Subscription> listSubscriptions() {
        Query query = manager.createQuery("SELECT a FROM Subscription a");
        return query.getResultList();
    }

    /**
     * get a subscription by searching for user
     * @param utente
     * @return
     */
    public Subscription getSubscriptionByUser(Utente utente) {
        getLogger().info("UID of Utente is " + utente.getUid());
        Subscription subscription = null;
        try {
            subscription = (Subscription) manager.createQuery("SELECT t FROM Subscription t WHERE t.utente = :id").setParameter("id", utente).getSingleResult();
        } catch (Exception e) {
            getLogger().warning("exception" + e);
        }
        return subscription;
    }

    /**
     * add a subscription with a certain given price
     * @param subscription
     * @param price
     * @return
     */
    public Subscription addSubscription(Subscription subscription, float price) {
        subscription.setPaid(false);
        subscription.setPrice(price);
        manager.persist(subscription);

        return subscription;
    }

    /**
     * set next state, and payment gateway field for a certain subscription
     * @param paymentGateway
     * @return
     */
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

    /**
     * Delete a certain subscription
     * @param subscription
     */
    public void deleteSubscription(Subscription subscription) {
        Subscription sub = manager.find(Subscription.class, subscription.getSid());
        manager.remove(sub);
    }

    /**
     * Update payment status.
     * @param subscription
     */
    public void updatePayment(Subscription subscription) {
        subscription.setPaid(true);
        manager.merge(subscription);
    }
}
