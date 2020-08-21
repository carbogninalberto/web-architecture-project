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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final String dir = System.getProperty("jboss.server.base.dir"); //user.dir
        System.out.println("current dir = " + dir);

        //listUserJDBC();

        //System.out.println("get " + uBean.getUser("fede@gmail.com").getName());

        List<Utente> utenti = uBean.listUsers();
        List<String> userNames = new ArrayList<>();

        for (Utente utenteTmp : utenti) {
            userNames.add("{" + utenteTmp.getName() + ", " + utenteTmp.getEmail() + "}" );
        }
        // init mapper
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writer().writeValueAsString(utenti);
        //Object to JSON Conversion
        //Student student = mapper.readValue(jsonString, Student.class);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        //out.println("users: " + userNames.toString());
        out.println(jsonResponse);
        out.close();
        out.flush();
    }

    protected void listUserJDBC() {
        String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
        String DB_URL = "jdbc:derby:C:/Users/Alberto/Desktop/Bakney/AssociazioneSportiva/src/main/resources/database;create=true";
        //  Database credentials
        final String USER = "admin";
        final String PASS = "admin";
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT t.email, t.name FROM ADMIN.\"Users\" t";
            ResultSet rs = stmt.executeQuery(sql);
            //STEP 5: Extract data from result set
            System.out.println("fetch size:" + rs.getFetchSize());
            while(rs.next()) {
                System.out.println("ok");
                //Retrieve by column name
                String email = rs.getString("email");
                String name = rs.getString("name");

                //Display values
                System.out.println(email + " " + name);
            }
            //STEP 6: Clean-up environment
            System.out.print("closing connection");
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try

    }
}
