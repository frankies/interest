package com.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A diagnostic servlet for testing JNDI datasource availability and
 * connectivity.
 * <p>
 * This servlet checks the status of configured JNDI datasources and optionally
 * tests their connectivity. It's designed for integration testing and system
 * health monitoring purposes. The servlet can read JNDI resource names from
 * a Tomcat context XML file or use a default set of resource names.
 * </p>
 * 
 * <h3>Features:</h3>
 * <ul>
 * <li>Checks JNDI resource availability in the naming context</li>
 * <li>Optionally tests database connectivity for DataSource resources</li>
 * <li>Reads resource names from Tomcat context XML files</li>
 * <li>Provides clear status reporting (OK, FAIL, NOT_A_DATASOURCE)</li>
 * <li>Handles errors gracefully with detailed error messages</li>
 * </ul>
 * 
 * <h3>URL Parameters:</h3>
 * <ul>
 * <li><code>connect=true</code> - enables connectivity testing</li>
 * </ul>
 * 
 * <h3>System Properties:</h3>
 * <ul>
 * <li><code>jndi.contextFile</code> - path to Tomcat context XML file</li>
 * <li><code>test.jndi.connect</code> - default connectivity testing
 * setting</li>
 * </ul>
 * 
 * @author Generated Servlet
 * @version 1.0
 * @since 1.0
 */
public class JndiCheckServlet extends HttpServlet {

    /**
     * Handles HTTP GET requests to check JNDI resource status.
     * <p>
     * This method performs comprehensive JNDI resource testing by:
     * </p>
     * <ol>
     * <li>Reading the list of JNDI resource names to check</li>
     * <li>Looking up each resource in the naming context</li>
     * <li>Verifying that resources are DataSource instances</li>
     * <li>Optionally testing database connectivity</li>
     * <li>Reporting status for each resource</li>
     * </ol>
     * 
     * <h3>Response Format:</h3>
     * <p>
     * Each line contains: <code>resourceName=STATUS [details]</code>
     * </p>
     * <ul>
     * <li><code>OK</code> - Resource found and accessible</li>
     * <li><code>OK (connected)</code> - Resource found and database connection
     * successful</li>
     * <li><code>NOT_A_DATASOURCE</code> - Resource exists but is not a
     * DataSource</li>
     * <li><code>FAIL</code> - Resource lookup or connection failed</li>
     * </ul>
     * 
     * @param req  the HTTP request object
     * @param resp the HTTP response object
     * @throws IOException if I/O error occurs during response writing
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");

        boolean checkConnection = Boolean.parseBoolean(req.getParameter("connect"));
        if (!checkConnection) {
            checkConnection = Boolean.parseBoolean(System.getProperty("test.jndi.connect", "false"));
        }

        try {
            String[] jndiNames = getJndiNames();

            PrintWriter out = resp.getWriter();
            InitialContext ic = new InitialContext();

            resp.setStatus(200);
            for (String name : jndiNames) {
                String fullName = "java:comp/env/" + name;
                try {
                    Object obj = ic.lookup(fullName);
                    if (!(obj instanceof DataSource)) {
                        out.println(name + "=NOT_A_DATASOURCE (" + (obj == null ? "null" : obj.getClass().getName())
                                + ")");
                        continue;
                    }

                    if (!checkConnection) {
                        out.println("❌" + name + "=NG");
                        continue;
                    }

                    DataSource ds = (DataSource) obj;
                    Connection c = null;
                    try {
                        c = ds.getConnection();
                        String product = "";
                        String version = "";
                        try {
                            java.sql.DatabaseMetaData md = c.getMetaData();
                            if (md != null) {
                                product = String.valueOf(md.getDatabaseProductName());
                                version = String.valueOf(md.getDatabaseProductVersion());
                            }
                        } catch (Exception ignored) {
                        }
                        out.println(name + "=✅OK (connected, db=" + product + ", version=" + version + ")");
                    } finally {
                        if (c != null) {
                            try {
                                c.close();
                            } catch (Exception ignored) {
                            }
                        }
                    }
                } catch (Exception e) {
                    out.println(name + "=FAIL (" + e.getClass().getSimpleName() + ": " + safeMessage(e) + ")");
                }
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().println("Servlet failure: " + e.getClass().getSimpleName() + ": " + safeMessage(e));
        }
    }

    /**
     * Retrieves the list of JNDI resource names to check.
     * <p>
     * This method first checks if a context XML file is specified via the
     * <code>jndi.contextFile</code> system property. If found and readable,
     * it parses the XML file to extract resource names. Otherwise, it returns
     * a default set of commonly used JNDI resource names.
     * </p>
     * 
     * @return array of JNDI resource names to check
     * @throws Exception if error occurs while reading the context XML file
     */
    private static String[] getJndiNames() throws Exception {
        String contextFile = System.getProperty("jndi.contextFile", "").trim();
        if (!contextFile.isEmpty()) {
            File f = new File(contextFile);
            if (f.isFile()) {
                return readJndiNamesFromContextXml(f);
            }
        }

        return new String[] { "jdbc/demo_db" };
    }

    /**
     * Parses a Tomcat context XML file to extract JNDI resource names.
     * <p>
     * This method reads a Tomcat context configuration file and extracts the
     * names of all &lt;Resource&gt; elements. This allows the servlet to
     * dynamically check whatever resources are actually configured for the
     * current deployment context.
     * </p>
     * 
     * @param xmlFile the Tomcat context XML file to parse
     * @return array of resource names found in the XML file
     * @throws Exception if XML parsing fails or file cannot be read
     */
    private static String[] readJndiNamesFromContextXml(File xmlFile) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
        NodeList resources = doc.getElementsByTagName("Resource");
        String[] names = new String[resources.getLength()];
        for (int i = 0; i < resources.getLength(); i++) {
            Element el = (Element) resources.item(i);
            names[i] = el.getAttribute("name");
        }
        return names;
    }

    /**
     * Safely extracts and cleans an exception message for output.
     * <p>
     * This method extracts the message from a throwable and removes
     * line breaks that could interfere with the output format. This
     * ensures error messages are displayed on a single line for
     * better readability in the servlet response.
     * </p>
     * 
     * @param t the throwable to extract message from
     * @return cleaned message string, empty string if message is null
     */
    private static String safeMessage(Throwable t) {
        String m = t.getMessage();
        if (m == null) {
            return "";
        }
        return m.replace('\r', ' ').replace('\n', ' ');
    }
}
