package com.carbogninalberto.util;

import com.carbogninalberto.ejb.SubscriptionBean;
import com.carbogninalberto.ejb.UtenteBean;
import com.carbogninalberto.itf.Logging;

import javax.naming.Context;
import javax.naming.NamingException;

public class InitialContext {

    public static Context ctx = null;
    public static SubscriptionBean subscriptionBean = null;
    public static UtenteBean utenteBean = null;

    public static Context getContext() throws NamingException {
        if (ctx == null)
            ctx = new javax.naming.InitialContext();
        return ctx;
    }

    public static void main() throws NamingException {
        getContext();
        if (subscriptionBean == null && utenteBean == null) {
            subscriptionBean = (SubscriptionBean) ctx.lookup("java:global/AssociazioneSportiva/AssociazioneSportiva-1.0/SubscriptionBean");
            utenteBean = (UtenteBean) ctx.lookup("java:global/AssociazioneSportiva/AssociazioneSportiva-1.0/UtenteBean");
        }
    }

    public static void refresh() throws NamingException {
        subscriptionBean = (SubscriptionBean) ctx.lookup("java:global/AssociazioneSportiva/AssociazioneSportiva-1.0/SubscriptionBean");
        utenteBean = (UtenteBean) ctx.lookup("java:global/AssociazioneSportiva/AssociazioneSportiva-1.0/UtenteBean");

    }


}
