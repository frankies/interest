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

/**
 * A simple HTTP servlet that displays system information.
 * <p>
 * This servlet provides a web interface to view system environment variables
 * and JVM system properties. It generates an HTML page with collapsible
 * sections
 * showing the current system state, which is useful for debugging and system
 * inspection purposes.
 * </p>
 * 
 * <h3>Features:</h3>
 * <ul>
 * <li>Displays all OS environment variables from System.getenv()</li>
 * <li>Displays all JVM system properties from System.getProperties()</li>
 * <li>HTML escapes all output to prevent XSS attacks</li>
 * <li>Uses collapsible details elements for better UX</li>
 * </ul>
 * 
 * @author Generated Servlet
 * @version 1.0
 * @since 1.0
 */
public class HelloServlet extends HttpServlet {

    /**
     * Handles HTTP GET requests and generates the system information page.
     * <p>
     * This method creates a complete HTML page that displays system information
     * in an organized, user-friendly format. The page includes:
     * </p>
     * <ul>
     * <li>A welcome message and description</li>
     * <li>Collapsible section for OS environment variables</li>
     * <li>Collapsible section for JVM system properties</li>
     * </ul>
     * <p>
     * The response is sent with UTF-8 character encoding and HTML content type
     * for proper internationalization support.
     * </p>
     * 
     * @param request  the HTTP request object
     * @param response the HTTP response object
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if I/O error occurs during response writing
     */
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
            out.println(
                    "<p><strong>javax.servlet.Servlet JAR Path:</strong> " + escapeHtml(getServletJarPath()) + "</p>");
            out.println("<p><strong>com.example.HelloServlet Class Path:</strong> " + escapeHtml(getHelloServletPath())
                    + "</p>");

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

    /**
     * Retrieves the file path of the servlet API JAR.
     * <p>
     * This method uses reflection to locate the JAR file containing the
     * javax.servlet classes. It is useful for debugging classpath issues
     * related to servlet API dependencies.
     * </p>
     * 
     * @return the file path of the servlet API JAR, or a message if not found
     */
    private String getServletJarPath() {
        try {
            Class<?> servletClass = Class.forName("javax.servlet.Servlet");
            java.net.URL location = servletClass.getProtectionDomain().getCodeSource().getLocation();
            return location != null ? location.getPath() : "Location not found";
        } catch (ClassNotFoundException e) {
            return "javax.servlet.Servlet not found";
        }
    }

    /**
     * Retrieves the file path of the HelloServlet class.
     * <p>
     * This method uses the ProtectionDomain to locate the directory or JAR file
     * containing the HelloServlet class. It is useful for debugging classpath
     * issues
     * and understanding where the compiled class is loaded from.
     * </p>
     * 
     * @return the file path of the HelloServlet class location, or a message if not
     *         found
     */
    private String getHelloServletPath() {
        try {
            Class<?> helloServletClass = Class.forName("com.example.HelloServlet");
            java.net.URL location = helloServletClass.getProtectionDomain().getCodeSource().getLocation();
            return location != null ? location.getPath() : "Location not found";
        } catch (ClassNotFoundException e) {
            return "com.example.HelloServlet not found";
        }
    }

    /**
     * Escapes HTML special characters to prevent XSS attacks.
     * <p>
     * This method converts potentially dangerous characters in user input
     * to their HTML entity equivalents. This is essential for safely displaying
     * user-provided content in HTML pages.
     * </p>
     * 
     * <h3>Escaped Characters:</h3>
     * <ul>
     * <li>&amp; becomes &amp;amp;</li>
     * <li>&lt; becomes &amp;lt;</li>
     * <li>&gt; becomes &amp;gt;</li>
     * <li>" becomes &amp;quot;</li>
     * <li>' becomes &amp;#39;</li>
     * </ul>
     * 
     * @param s the input string to escape, may be null
     * @return the escaped string safe for HTML output, empty string if input is
     *         null
     */
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

    /**
     * Prints all system environment variables in sorted order.
     * <p>
     * This method retrieves all environment variables from the operating system
     * and prints them as key=value pairs. The output is sorted alphabetically
     * by key name for easier reading, and all values are HTML-escaped for safety.
     * </p>
     * 
     * @param out the PrintWriter to write the environment variables to
     */
    private static void printEnvVars(PrintWriter out) {
        Map<String, String> env = System.getenv();
        List<String> keys = new ArrayList<String>(env.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            String value = env.get(key);
            out.println(escapeHtml(key) + "=" + escapeHtml(value));
        }
    }

    /**
     * Prints all JVM system properties in sorted order.
     * <p>
     * This method retrieves all system properties from the Java Virtual Machine
     * and prints them as key=value pairs. The output is sorted alphabetically
     * by key name for easier reading, and all values are HTML-escaped for safety.
     * System properties include JVM configuration, Java version information,
     * file system paths, and other runtime settings.
     * </p>
     * 
     * @param out the PrintWriter to write the system properties to
     */
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
}
