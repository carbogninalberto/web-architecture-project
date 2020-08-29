package com.carbogninalberto.servlet.subscription;

import com.carbogninalberto.entity.Subscription;
import com.carbogninalberto.entity.Utente;
import com.carbogninalberto.itf.Logging;
import com.carbogninalberto.util.*;
import com.carbogninalberto.util.response.Response;
import com.carbogninalberto.util.response.ResponseSubscriptionList;
import com.carbogninalberto.util.response.ResponseSubscriptionStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/subscription/add", "/subscription/list"})
public class SubscriptionAddServlet extends HttpServlet implements Logging {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        try {
            InitialContext.main();
        } catch (Exception e) {
            getLogger().severe(e.toString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // define response type
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        // logging
        getLogger().info(this.getServletName() + " subscription list");

        try {
            // logging
            getLogger().info("BEAN STATUS " + InitialContext.subscriptionBean.getStatus());

            List<Subscription> subscriptions = InitialContext.subscriptionBean.listSubscriptions();

            // response
            resp.setStatus(200);
            ResponseSubscriptionList msg = new ResponseSubscriptionList("list of subscriptions", subscriptions);
            String msgJson = mapper.writeValueAsString(msg);
            out.println(msgJson);

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

        // logging
        getLogger().info(this.getServletName() + " subscription status");

        try {
            // logging
            getLogger().info("BEAN STATUS " + InitialContext.subscriptionBean.getStatus());

            // Obtaining the Body of the request
            BufferedReader body = req.getReader();
            // read the buffer and converting it to object
            String json = body.lines().collect(Collectors.joining());
            Utente utente = mapper.readValue(json, Utente.class);
            utente = InitialContext.utenteBean.getUtente(utente.getEmail());
            Subscription subscription = mapper.readValue(json, Subscription.class);
            subscription.setUtente(utente);

            // mocked-up price of subscription
            Subscription sub = InitialContext.subscriptionBean.getSubscriptionByUser(utente);

            if (sub != null)
                InitialContext.subscriptionBean.deleteSubscription(sub);
            // mocked price of subscription
            InitialContext.subscriptionBean.addSubscription(subscription, (float) 23.50);
            InitialContext.subscriptionBean.setAdvanceStatus("");

            // response
            resp.setStatus(200);
            ResponseSubscriptionStatus msg = new ResponseSubscriptionStatus("subscription", InitialContext.subscriptionBean.getStatus().toString(), InitialContext.subscriptionBean.getPaymentGateway());
            String msgJson = mapper.writeValueAsString(msg);
            out.println(msgJson);

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
