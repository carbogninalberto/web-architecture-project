package com.carbogninalberto.servlet;

import com.carbogninalberto.itf.Logging;
import com.carbogninalberto.util.response.Response;
import com.carbogninalberto.util.response.ResponseRealTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/memory-usage"})
public class MemoryUsageServlet extends HttpServlet implements Logging {

    // Jackson Mapper
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // define response type
        resp.setContentType("application/json");
        // define writer
        PrintWriter out = resp.getWriter();

        // logging
        getLogger().info(this.getServletName() + " real time memory usage data.");

        // business logic
        try {
            // getting usage data
            long usage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            // response
            resp.setStatus(200);
            ResponseRealTime msg = new ResponseRealTime("real time memory usage.", usage);
            String msgJson = mapper.writeValueAsString(msg);
            out.println(msgJson);

        } catch (Exception e) {
            // logging
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
