package com.carbogninalberto.servlet.user;

import com.carbogninalberto.ejb.UtenteBean;
import com.carbogninalberto.entity.Utente;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/user/list"})
public class ListUtenti extends HttpServlet {

    @EJB
    UtenteBean uBean;
    final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        List<Utente> utenti = uBean.listUsers();
        List<String> userNames = new ArrayList<>();

        for (Utente utenteTmp : utenti) {
            userNames.add("{" + utenteTmp.getName() + ", " + utenteTmp.getEmail() + "}" );
        }

        String jsonResponse = mapper.writer().writeValueAsString(utenti);


        out.println(jsonResponse);
        out.close();
        out.flush();
    }
}
