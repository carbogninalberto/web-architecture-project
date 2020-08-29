package com.carbogninalberto.util.response;

import com.carbogninalberto.entity.Subscription;
import java.io.Serializable;
import java.util.List;

public class ResponseSubscriptionList extends Response implements Serializable {

    List<Subscription> subscriptions;

    public ResponseSubscriptionList(String msg, List<Subscription> subscriptions) {
        super(msg);
        this.subscriptions = subscriptions;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
