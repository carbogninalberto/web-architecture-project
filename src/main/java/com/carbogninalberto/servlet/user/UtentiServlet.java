package com.carbogninalberto.servlet.user;

import com.carbogninalberto.ejb.HelloWorldBean;
import com.carbogninalberto.ejb.UtenteBean;
import com.carbogninalberto.entity.Utente;
import com.carbogninalberto.itf.Logging;
import com.carbogninalberto.obj.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/utenti"})
public class UtentiServlet extends HttpServlet implements Logging {

    private UtenteBean utenteBean;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        try {
            Context ctx = new InitialContext();
            utenteBean = (UtenteBean) ctx.lookup("java:global/AssociazioneSportiva/AssociazioneSportiva-1.0/UtenteBean");
            getLogger().info("LOOKUP " + utenteBean.getClass().toString());
            BufferedReader body = req.getReader();
            Utente utente = mapper.readValue(body.lines().collect(Collectors.joining()), Utente.class);
            utenteBean.addUtente(utente);
            resp.setStatus(200);
            Response msg = new Response("Utente Creato con Successo");
            String msgJson = mapper.writeValueAsString(msg);
            out.println(msgJson);
            out.close();
            out.flush();

        } catch (Exception e) {
            getLogger().warning("Exception: " + e.getMessage());
            resp.setStatus(500);
            Response msg = new Response(e.getMessage());
            String msgJson = mapper.writeValueAsString(msg);
            out.println(msgJson);
            out.close();
            out.flush();
        }
    }
}
