package com.carbogninalberto.servlet.auth;

import com.carbogninalberto.ejb.UtenteBean;
import com.carbogninalberto.entity.Utente;
import com.carbogninalberto.itf.Logging;
import com.carbogninalberto.util.Response;
import com.carbogninalberto.util.ResponseUser;
import com.carbogninalberto.util.UserInfo;
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

@WebServlet(urlPatterns = {"/login", "/session/delete"})
public class SessionServlet extends HttpServlet implements Logging {

    private UtenteBean utenteBean;
    private final ObjectMapper mapper = new ObjectMapper();

    private void getBean() throws NamingException {
        Context ctx = new InitialContext();
        utenteBean = (UtenteBean) ctx.lookup("java:global/AssociazioneSportiva/AssociazioneSportiva-1.0/UtenteBean");
        getLogger().info("LOOKUP " + utenteBean.getClass().toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // define response type
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        getLogger().info(this.getServletName() + " login user");

        try {
            getBean();

            // Obtaining the Body of the request
            BufferedReader body = req.getReader();
            // read the buffer and converting it to object
            Utente utente = mapper.readValue(body.lines().collect(Collectors.joining()), Utente.class);

            // business logic
            UserInfo userInfo = utenteBean.checkPasswordUtente(utente);
            if (userInfo.isLogged()) {
                // updating admin information
                utente.setAdmin(utenteBean.getUtente(utente.getEmail()).getAdmin());

                HttpSession session = req.getSession(true);
                session.setAttribute("auth", utente.getEmail());
                session.setAttribute("admin", utente.getAdmin());
                session.setMaxInactiveInterval(3600 * 24 * 14);

                resp.setStatus(200);
                ResponseUser msg = new ResponseUser("Logged in. Redirecting...", userInfo.getUtente());
                String msgJson = mapper.writeValueAsString(msg);
                out.println(msgJson);
            } else {
                resp.setStatus(500);
                Response msg = new Response("Password is not matching.");
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // define response type
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        getLogger().info(this.getServletName() + " delete HttpSession");

        try {
            HttpSession session = req.getSession(false);
            if (session != null)
                session.invalidate();

            resp.setStatus(200);
            Response msg = new Response("session removed.");
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
