package com.example.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.deploy.NamingResources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Utility class for testing with embedded Tomcat servers.
 * <p>
 * This final utility class provides static methods for creating, configuring,
 * and managing embedded Tomcat server instances for integration testing.
 * It handles the complexities of programmatically setting up Tomcat with
 * custom configurations, JNDI resources, and servlet mappings.
 * </p>
 * 
 * <h3>Key Features:</h3>
 * <ul>
 * <li>Creates lightweight embedded Tomcat instances</li>
 * <li>Supports JNDI naming context configuration</li>
 * <li>Handles automatic port allocation</li>
 * <li>Provides HTTP client utilities for testing</li>
 * <li>Manages temporary directories and cleanup</li>
 * <li>Parses Tomcat context XML configurations</li>
 * </ul>
 * 
 * <h3>Typical Usage:</h3>
 * 
 * <pre>
 * RunningTomcat server = TomcatTestSupport.start("/myapp", true, ctx -> {
 *     Tomcat.addServlet(ctx, "myServlet", new MyServlet());
 *     ctx.addServletMapping("/test", "myServlet");
 * });
 * 
 * String response = TomcatTestSupport.httpGetText(server.getPort(), "/myapp/test");
 * server.close();
 * </pre>
 * 
 * @author Generated Utility
 * @version 1.0
 * @since 1.0
 */
public final class TomcatTestSupport {

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private TomcatTestSupport() {
    }

    /**
     * Starts an embedded Tomcat server with custom configuration.
     * <p>
     * This method creates and starts a complete embedded Tomcat server instance
     * with the following features:
     * </p>
     * <ul>
     * <li>Automatic port allocation (uses port 0 for dynamic assignment)</li>
     * <li>Temporary directory creation for Tomcat's working files</li>
     * <li>Optional JNDI naming context support</li>
     * <li>Custom servlet and component configuration via ContextConfigurer</li>
     * </ul>
     * 
     * @param contextPath  the web application context path (use "/" for root)
     * @param enableNaming whether to enable JNDI naming support
     * @param configure    callback interface for configuring servlets and
     *                     components, may be null
     * @return RunningTomcat instance representing the started server
     * @throws Exception if server startup, configuration, or port allocation fails
     */
    public static RunningTomcat start(String contextPath, boolean enableNaming, ContextConfigurer configure)
            throws Exception {
        Tomcat tomcat = new Tomcat();
        File tempDir = File.createTempFile("tomcat-test-", "");
        tempDir.delete();
        tempDir.mkdir();
        tomcat.setBaseDir(tempDir.getAbsolutePath());
        if (enableNaming) {
            tomcat.enableNaming();
        }

        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(0);
        tomcat.getService().addConnector(connector);
        tomcat.setConnector(connector);

        File docBase = File.createTempFile("tomcat-test-docBase-", "");
        docBase.delete();
        docBase.mkdir();
        String adjustedPath = contextPath;
        if ("/".equals(contextPath)) {
            adjustedPath = "";
        }
        Context context = tomcat.addContext(adjustedPath, docBase.getAbsolutePath());

        if (configure != null) {
            configure.configure(context);
        }

        tomcat.start();
        int port = tomcat.getConnector().getLocalPort();
        if (port <= 0) {
            stopQuietly(tomcat);
            throw new IllegalStateException("Failed to allocate HTTP port");
        }
        return new RunningTomcat(tomcat, port);
    }

    /**
     * Performs an HTTP GET request and returns the response body as text.
     * <p>
     * This utility method simplifies making HTTP requests to test endpoints
     * by handling connection setup, error handling, and response reading.
     * The method includes reasonable timeouts and proper resource cleanup.
     * </p>
     * 
     * <h3>Configuration:</h3>
     * <ul>
     * <li>Connect timeout: 10 seconds</li>
     * <li>Read timeout: 10 seconds</li>
     * <li>Character encoding: UTF-8</li>
     * </ul>
     * 
     * @param port the port number of the target server
     * @param path the request path (should start with "/")
     * @return the response body as a string
     * @throws Exception if the request fails or returns a non-200 status code
     */
    public static String httpGetText(int port, String path) throws Exception {
        URL url = new URL("http://localhost:" + port + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestMethod("GET");

        int code = conn.getResponseCode();
        String body;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    code >= 200 && code < 400 ? conn.getInputStream() : conn.getErrorStream(),
                    "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            body = sb.toString();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {
                }
            }
        }

        if (code != 200) {
            throw new AssertionError("HTTP " + code + " for GET " + path + "\n" + body);
        }
        return body;
    }

    /**
     * Adds JNDI resources to a Tomcat context based on XML configuration.
     * <p>
     * This method parses a Tomcat context XML file and programmatically adds
     * all defined &lt;Resource&gt; elements to the given Tomcat context. This
     * allows tests to use the same JNDI configuration as the production
     * environment without requiring a full deployment.
     * </p>
     * 
     * <h3>Supported Resource Attributes:</h3>
     * <ul>
     * <li>name - JNDI resource name (required)</li>
     * <li>type - Resource type (defaults to javax.sql.DataSource)</li>
     * <li>auth - Authentication method (defaults to Container)</li>
     * <li>driverClassName - Database driver class</li>
     * <li>url - Database connection URL</li>
     * <li>username - Database username</li>
     * <li>password - Database password</li>
     * <li>maxTotal, maxIdle, maxActive, maxWait - Connection pool settings</li>
     * </ul>
     * 
     * @param ctx     the Tomcat context to configure with JNDI resources
     * @param xmlFile the context XML file containing resource definitions
     * @throws Exception if XML parsing fails or resource configuration is invalid
     */
    public static void addJndiResourcesFromContextXml(Context ctx, File xmlFile) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
        NodeList resources = doc.getElementsByTagName("Resource");

        for (int i = 0; i < resources.getLength(); i++) {
            Element el = (Element) resources.item(i);

            String name = el.getAttribute("name");
            if (name == null || name.trim().isEmpty()) {
                continue;
            }

            // Use Context resource setting directly instead of ContextResource
            try {
                String type = attrOrDefault(el, "type", "javax.sql.DataSource");
                String auth = attrOrDefault(el, "auth", "Container");

                // Create resource directly using Context methods
                NamingResources namingRes = ctx.getNamingResources();
                org.apache.catalina.deploy.ContextResource cr = new org.apache.catalina.deploy.ContextResource();
                cr.setName(name);
                cr.setType(type);
                cr.setAuth(auth);

                // Force a known factory for embedded Tomcat tests.
                cr.setProperty("factory", "org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory");

                copyIfPresent(el, cr, "driverClassName");
                copyIfPresent(el, cr, "url");
                copyIfPresent(el, cr, "username");
                copyIfPresent(el, cr, "password");
                copyIfPresent(el, cr, "maxTotal");
                copyIfPresent(el, cr, "maxIdle");
                // Tomcat 7 DBCP uses different property names
                copyIfPresent(el, cr, "maxActive");
                copyIfPresent(el, cr, "maxWait");

                namingRes.addResource(cr);
            } catch (Exception e) {
                // Log and continue with other resources if one fails
                System.err.println("Failed to add JNDI resource '" + name + "': " + e.getMessage());
            }
        }
    }

    /**
     * Copies an XML attribute to a context resource if the attribute is present and
     * non-empty.
     * 
     * @param el   the XML element to read the attribute from
     * @param cr   the context resource to set the property on
     * @param attr the attribute name to copy
     */
    private static void copyIfPresent(Element el, org.apache.catalina.deploy.ContextResource cr, String attr) {
        String v = el.getAttribute(attr);
        if (v != null && !v.trim().isEmpty()) {
            cr.setProperty(attr, v);
        }
    }

    /**
     * Gets an XML attribute value or returns a default value if the attribute is
     * missing or empty.
     * 
     * @param el   the XML element to read the attribute from
     * @param attr the attribute name to read
     * @param def  the default value to return if attribute is missing or empty
     * @return the attribute value or default value
     */
    private static String attrOrDefault(Element el, String attr, String def) {
        String v = el.getAttribute(attr);
        if (v == null || v.trim().isEmpty()) {
            return def;
        }
        return v;
    }

    /**
     * Safely stops and destroys a Tomcat instance, ignoring any exceptions.
     * <p>
     * This utility method ensures that Tomcat cleanup operations don't fail
     * the test execution if they encounter errors. This is particularly useful
     * during test teardown where we want to clean up resources regardless of
     * their current state.
     * </p>
     * 
     * @param t the Tomcat instance to stop, may be null
     */
    private static void stopQuietly(Tomcat t) {
        if (t == null) {
            return;
        }
        try {
            t.stop();
        } catch (Exception ignored) {
        }
        try {
            t.destroy();
        } catch (Exception ignored) {
        }
    }

    /**
     * Functional interface for configuring Tomcat contexts during server startup.
     * <p>
     * This interface allows callers to provide custom configuration logic
     * that will be executed during the Tomcat context setup process. Typically
     * used to add servlets, filters, and other web components to the context.
     * </p>
     */
    public interface ContextConfigurer {
        /**
         * Configures the given Tomcat context with servlets and other web components.
         * 
         * @param ctx the Tomcat context to configure
         * @throws Exception if configuration fails
         */
        void configure(Context ctx) throws Exception;
    }

    /**
     * Represents a running embedded Tomcat server instance.
     * <p>
     * This class encapsulates a started Tomcat server and provides access to
     * its allocated port number and lifecycle management. The class ensures
     * proper cleanup and prevents multiple shutdown attempts.
     * </p>
     */
    public static final class RunningTomcat {
        private final Tomcat tomcat;
        private final int port;
        private boolean closed = false;

        /**
         * Creates a new RunningTomcat instance.
         * 
         * @param tomcat the underlying Tomcat server instance
         * @param port   the allocated HTTP port number
         */
        private RunningTomcat(Tomcat tomcat, int port) {
            this.tomcat = tomcat;
            this.port = port;
        }

        /**
         * Returns the HTTP port number that the server is listening on.
         * 
         * @return the allocated port number
         */
        public int getPort() {
            return port;
        }

        /**
         * Stops the Tomcat server and releases all resources.
         * <p>
         * This method is idempotent and can be called multiple times safely.
         * Any exceptions during shutdown are ignored to ensure cleanup completes.
         * </p>
         * 
         * @throws Exception if shutdown fails, but exceptions are typically ignored
         */
        public void close() throws Exception {
            if (tomcat == null || closed) {
                closed = true;
                return;
            }
            closed = true;
            try {
                tomcat.stop();
            } catch (Exception ignored) {
            }
            try {
                tomcat.destroy();
            } catch (Exception ignored) {
            }
        }
    }
}
