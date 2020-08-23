package com.carbogninalberto.servlet.user;

import com.carbogninalberto.ejb.UtenteBean;
import com.carbogninalberto.entity.Utente;
import com.carbogninalberto.itf.Logging;
import com.carbogninalberto.util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/user", "/user/add"})
public class UtentiServlet extends HttpServlet implements Logging {

    private UtenteBean utenteBean;
    // Jackson Mapper
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();

    }

    private void getBean() throws NamingException {
        Context ctx = new InitialContext();
        utenteBean = (UtenteBean) ctx.lookup("java:global/AssociazioneSportiva/AssociazioneSportiva-1.0/UtenteBean");
        getLogger().info("LOOKUP " + utenteBean.getClass().toString());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // define response type
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        // try to get by JNDI the required bean
        try {
            getBean();
            // Obtaining the Body of the request
            BufferedReader body = req.getReader();
            // read the buffer and converting it to object
            Utente utente = mapper.readValue(body.lines().collect(Collectors.joining()), Utente.class);

            // Business logic
            utenteBean.addUtente(utente);

            // response
            resp.setStatus(200);
            Response msg = new Response("Success! Click on Login now!");
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
