package com.carbogninalberto.servlet.subscription;

import com.carbogninalberto.ejb.SubscriptionBean;
import com.carbogninalberto.ejb.UtenteBean;
import com.carbogninalberto.entity.Subscription;
import com.carbogninalberto.entity.Utente;
import com.carbogninalberto.itf.Logging;
import com.carbogninalberto.util.Response;
import com.carbogninalberto.util.ResponseSubscriptionStatus;
import com.carbogninalberto.util.SubscriptionStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/subscription/status"})
public class Status extends HttpServlet implements Logging {

    private UtenteBean utenteBean;
    private SubscriptionBean subscriptionBean;
    private final ObjectMapper mapper = new ObjectMapper();

    private void getSubscriptionBean() throws NamingException {
        Context ctx = new InitialContext();
        subscriptionBean = (SubscriptionBean) ctx.lookup("java:global/AssociazioneSportiva/AssociazioneSportiva-1.0/SubscriptionBean");
        getLogger().info("LOOKUP " + subscriptionBean.getClass().toString());
        utenteBean = (UtenteBean) ctx.lookup("java:global/AssociazioneSportiva/AssociazioneSportiva-1.0/UtenteBean");
        getLogger().info("LOOKUP " + utenteBean.getClass().toString());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // define response type
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        getLogger().info(this.getServletName() + " subscription status");

        try {
            getSubscriptionBean();

            String email = (String) req.getAttribute("email");
            Utente utente = utenteBean.getUtente(email);
            Subscription subscription = subscriptionBean.getSubscriptionByUser(utente);

            if (null != subscription) {
                if (subscription.isPaid() && subscriptionBean.getStatus() != SubscriptionStatus.CONFIRMED)
                    subscriptionBean.setStatus(SubscriptionStatus.CONFIRMED);

                resp.setStatus(200);
                ResponseSubscriptionStatus msg = new ResponseSubscriptionStatus("subscription", subscriptionBean.getStatus().toString(), subscriptionBean.getPaymentGateway());
                String msgJson = mapper.writeValueAsString(msg);
                out.println(msgJson);
            } else {
                resp.setStatus(500);
                Response msg = new Response("Error on getting subscription.");
                String msgJson = mapper.writeValueAsString(msg);
                out.println(msgJson);
            }
        } catch (Exception e) {
            getLogger().warning("Exception: " + e.getMessage());

            // response
            resp.setStatus(500);
            Response msg = new Response(e.getMessage());
            String msgJson = mapper.writeValueAsString(msg);
            out.println(msgJson);
        } finally {
            out.close();
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // define response type
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        getLogger().info(this.getServletName() + " subscription status");

        try {
            getSubscriptionBean();

            // Obtaining the Body of the request
            BufferedReader body = req.getReader();
            // read the buffer and converting it to object
            Utente utente = mapper.readValue(body.lines().collect(Collectors.joining()), Utente.class);
            utente = utenteBean.getUtente(utente.getEmail());
            Subscription subscription = subscriptionBean.getSubscriptionByUser(utente);

            // read payment gateway from json body
            JsonNode parent = new ObjectMapper().readTree(body.lines().collect(Collectors.joining()));
            String paymentGateway = parent.path("paymentGateway").asText();

            if (null != subscription) {
                SubscriptionStatus nextStatus = subscriptionBean.setAdvanceStatus(paymentGateway != null ? paymentGateway :"");
                if (subscriptionBean.getStatus() != SubscriptionStatus.PAYMENT && nextStatus == SubscriptionStatus.CONFIRMED) {
                    subscriptionBean.updatePayment(subscription);
                }

                resp.setStatus(200);
                ResponseSubscriptionStatus msg = new ResponseSubscriptionStatus("subscription", subscriptionBean.getStatus().toString(), subscriptionBean.getPaymentGateway());
                String msgJson = mapper.writeValueAsString(msg);
                out.println(msgJson);
            } else {
                resp.setStatus(500);
                Response msg = new Response("Error on getting subscription.");
                String msgJson = mapper.writeValueAsString(msg);
                out.println(msgJson);
            }
        } catch (Exception e) {
            getLogger().warning("Exception: " + e.getMessage());

            // response
            resp.setStatus(500);
            Response msg = new Response(e.getMessage());
            String msgJson = mapper.writeValueAsString(msg);
            out.println(msgJson);
        } finally {
            out.close();
            out.flush();
        }
    }


}
