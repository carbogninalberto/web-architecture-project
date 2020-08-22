package com.carbogninalberto.servlet.auth;

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
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/login"})
public class SessionServlet extends HttpServlet implements Logging {

    private UtenteBean utenteBean;
    private final ObjectMapper mapper = new ObjectMapper();

    private void getBean() throws NamingException {
        Context ctx = new InitialContext();
        utenteBean = (UtenteBean) ctx.lookup("java:global/AssociazioneSportiva/AssociazioneSportiva-1.0/UtenteBean");
        getLogger().info("LOOKUP " + utenteBean.getClass().toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // define response type
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            getBean();

            // Obtaining the Body of the request
            BufferedReader body = req.getReader();
            // read the buffer and converting it to object
            Utente utente = mapper.readValue(body.lines().collect(Collectors.joining()), Utente.class);

            // business logic
            boolean isPasswordOk = utenteBean.checkPasswordUtente(utente);
            if (isPasswordOk) {
                HttpSession session = req.getSession(true);
                session.setAttribute("auth", utente.getEmail());
                session.setAttribute("admin", utente.getAdmin());
                session.setMaxInactiveInterval(3600 * 24 * 14);


                resp.setStatus(200);
                Response msg = new Response("Logged in.");
                String msgJson = mapper.writeValueAsString(msg);
                out.println(msgJson);
            } else {
                throw new ServletException("Password is not matching.");
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
