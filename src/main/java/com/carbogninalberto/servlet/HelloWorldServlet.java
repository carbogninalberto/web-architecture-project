package com.carbogninalberto.servlet;

import com.carbogninalberto.ejb.HelloWorldBean;
import com.carbogninalberto.ejb.UtenteBean;
import com.carbogninalberto.entity.Utente;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/HelloWorldServlet"})
public class HelloWorldServlet extends HttpServlet {

    @EJB
    HelloWorldBean myBean;
    HelloWorldBean hwBean;
    @EJB
    UtenteBean uBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();

        // JNDI instead of dependency injection for stateful ejbs
        try {
            Context ctx = new InitialContext();
            hwBean = (HelloWorldBean) ctx.lookup("java:global/AssociazioneSportiva/AssociazioneSportiva-1.0/HelloWorldBean");
            out.println("LOOKUP: " + hwBean.sayHello("JNDI Bean!"));
        } catch (Exception e) {
            out.println("Exception: " + e);
        }

        uBean.addUtente(req.getParameter("name"), "Alberto Carbognin", "abcd");

        List<Utente> utentes = uBean.listUsers();
        List<String> userNames = new ArrayList<>();

        for (Utente utenteTmp : utentes) {
            userNames.add("{" + utenteTmp.getName() + ", " + utenteTmp.getEmail() + "}" );
        }

        out.println("users: " + userNames.toString());

        out.println(myBean.sayHello(req.getParameter("name")));
    }
}
