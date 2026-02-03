package com.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class HelloServlet extends HttpServlet {

    private static String escapeHtml(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&#39;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    private static void printEnvVars(PrintWriter out) {
        Map<String, String> env = System.getenv();
        List<String> keys = new ArrayList<String>(env.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            String value = env.get(key);
            out.println(escapeHtml(key) + "=" + escapeHtml(value));
        }
    }

    private static void printSystemProperties(PrintWriter out) {
        Properties props = System.getProperties();
        List<String> keys = new ArrayList<String>();
        for (Object k : props.keySet()) {
            keys.add(String.valueOf(k));
        }
        Collections.sort(keys);
        for (String key : keys) {
            String value = props.getProperty(key);
            out.println(escapeHtml(key) + "=" + escapeHtml(value));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\" />");
            out.println("<title>Hello Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Hello, Maven Web Project!</h1>");
            out.println("<p>This is a simple Maven web application with OAuth2 support.</p>");

            out.println("<details>");
            out.println("<summary>OS Environment Variables (System.getenv)</summary>");
            out.println("<pre>");
            printEnvVars(out);
            out.println("</pre>");
            out.println("</details>");

            out.println("<details>");
            out.println("<summary>JVM System Properties (System.getProperties)</summary>");
            out.println("<pre>");
            printSystemProperties(out);
            out.println("</pre>");
            out.println("</details>");

            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }
}
