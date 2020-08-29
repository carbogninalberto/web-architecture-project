package com.carbogninalberto.servlet.subscription;

import com.carbogninalberto.entity.Subscription;
import com.carbogninalberto.entity.Utente;
import com.carbogninalberto.itf.Logging;
import com.carbogninalberto.util.InitialContext;
import com.carbogninalberto.util.response.Response;
import com.carbogninalberto.util.response.ResponseSubscriptionStatus;
import com.carbogninalberto.util.response.SubscriptionStatus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/subscription/status"})
public class SubscriptionStatusServlet extends HttpServlet implements Logging {

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
        getLogger().info(this.getServletName() + " subscription status");

        try {
            // logging
            getLogger().info("BEAN STATUS " + InitialContext.subscriptionBean.getStatus());

            String email = (String) req.getParameter("email");
            Utente utente = InitialContext.utenteBean.getUtente(email);
            getLogger().info("UID: " + utente.getUid());
            Subscription subscription = InitialContext.subscriptionBean.getSubscriptionByUser(utente);

            // check if subscription is paid and confirmed
            if (null != subscription
                    && subscription.isPaid()
                    && InitialContext.subscriptionBean.getStatus() != SubscriptionStatus.CONFIRMED) {
                    InitialContext.subscriptionBean.setStatus(SubscriptionStatus.CONFIRMED);
            }

            // response
            resp.setStatus(200);
            ResponseSubscriptionStatus msg = new ResponseSubscriptionStatus("subscription",
                    InitialContext.subscriptionBean.getStatus().toString(),
                    InitialContext.subscriptionBean.getPaymentGateway());
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
            // Obtaining the Body of the request
            BufferedReader body = req.getReader();
            // read the buffer and converting it to object
            String json = body.lines().collect(Collectors.joining());
            Utente utente = mapper.readValue(json, Utente.class);
            utente = InitialContext.utenteBean.getUtente(utente.getEmail());
            Subscription subscription = InitialContext.subscriptionBean.getSubscriptionByUser(utente);

            // read payment gateway from json body
            JsonNode parent = mapper.readTree(json);
            String paymentGateway = parent.path("paymentGateway").asText();

            if (null != subscription) {
                SubscriptionStatus nextStatus = InitialContext.subscriptionBean.setAdvanceStatus(paymentGateway != null ? paymentGateway :"");
                if (InitialContext.subscriptionBean.getStatus() != SubscriptionStatus.PAYMENT
                        && nextStatus == SubscriptionStatus.CONFIRMED) {
                    InitialContext.subscriptionBean.updatePayment(subscription);
                }
            }

            // response
            resp.setStatus(200);
            ResponseSubscriptionStatus msg = new ResponseSubscriptionStatus("subscription",
                    InitialContext.subscriptionBean.getStatus().toString(),
                    InitialContext.subscriptionBean.getPaymentGateway());
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
