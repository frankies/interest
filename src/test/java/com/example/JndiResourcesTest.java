package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.example.base.BaseTomcatTest;
import com.example.utils.TomcatTestSupport;

/**
 * Integration test class for testing JNDI resource configuration.
 * <p>
 * This test class verifies that JNDI datasource resources can be correctly
 * configured and accessed
 * under different contextPath configurations. The tests use H2 in-memory
 * databases as mock data sources
 * and dynamically create Tomcat context configuration files in the system
 * temporary directory.
 * </p>
 * 
 * <h3>Test Scenarios:</h3>
 * <ul>
 * <li>JNDI resource access under root path ("/")</li>
 * <li>JNDI resource access under default path configuration</li>
 * <li>JNDI resource access under custom path ("/test")</li>
 * </ul>
 * 
 * <h3>Tested JNDI Resource:</h3>
 * <ul>
 * <li>jdbc/demo_db - Primary data management system datasource</li>
 * </ul>
 * 
 * @author Generated Test
 * @version 1.0
 * @since 1.0
 */
public class JndiResourcesTest extends BaseTomcatTest {

    private String contextPath;
    private File temporaryContextXml;

    /**
     * Tests JNDI resource lookup functionality under root path ("/").
     * <p>
     * This test verifies that when the application is deployed under the root path,
     * all configured JNDI datasource resources can be properly bound and accessed.
     * The test creates a temporary Tomcat context configuration, starts an embedded
     * Tomcat server, and verifies JNDI resource availability through HTTP requests.
     * </p>
     * 
     * @throws Exception if any exception occurs during testing, including server
     *                   startup failure,
     *                   HTTP request failure, or JNDI resource configuration errors
     */
    @Test
    public void shouldLookupAllJndiResourcesWithRootPath() throws Exception {
        setupJndiContextXml("/");
        String body = TomcatTestSupport.httpGetText(server.getPort(), contextPath + "/__jndi");
        assertJndiResourcesWork(body);
    }

    /**
     * Tests JNDI resource lookup functionality under default path configuration.
     * <p>
     * This test verifies that when no contextPath is explicitly specified (passing
     * null),
     * all JNDI datasource resources work correctly under the default path
     * configuration.
     * This scenario typically corresponds to the default deployment scenario of the
     * application.
     * </p>
     * 
     * @throws Exception if any exception occurs during testing, including default
     *                   configuration parsing failure,
     *                   server startup failure, or JNDI resource binding errors
     */
    @Test
    public void shouldLookupAllJndiResourcesWithDefaultPath() throws Exception {
        setupJndiContextXml(null); // Use default contextPath value
        String body = TomcatTestSupport.httpGetText(server.getPort(), contextPath + "/__jndi");
        assertJndiResourcesWork(body);
    }

    /**
     * Tests JNDI resource lookup functionality under custom path ("/test").
     * <p>
     * This test verifies that when the application is deployed under a custom
     * context path,
     * JNDI resource configuration remains effective. This scenario typically occurs
     * in
     * multi-application deployment environments where each application has its own
     * independent context path.
     * </p>
     * 
     * @throws Exception if any exception occurs during testing, including custom
     *                   path configuration failure,
     *                   context mapping errors, or JNDI resource access failures
     */
    @Test
    public void shouldLookupAllJndiResourcesWithTestPath() throws Exception {
        setupJndiContextXml("/test");
        String body = TomcatTestSupport.httpGetText(server.getPort(), contextPath + "/__jndi");
        assertJndiResourcesWork(body);
    }

    /**
     * Cleans up temporary resources created during testing.
     * <p>
     * This method is automatically called after each test method execution and is
     * responsible
     * for cleaning up the following resources:
     * </p>
     * <ul>
     * <li>Shutdown and destroy embedded Tomcat server instance</li>
     * <li>Delete temporarily created context configuration XML file</li>
     * </ul>
     * <p>
     * Ensures tests are independent of each other, avoiding resource leaks and
     * inter-test interference.
     * </p>
     * 
     * @throws Exception if exceptions occur during cleanup, but exceptions are
     *                   caught to ensure other cleanup operations continue
     */
    @After
    public void cleanupTemporaryFiles() throws Exception {
        // Clean up server
        if (server != null) {
            try {
                server.close();
            } catch (Exception ignored) {
            }
            server = null;
        }

        // Clean up temporary context XML file if we created it
        if (temporaryContextXml != null && temporaryContextXml.exists()) {
            temporaryContextXml.delete();
        }
    }

    /**
     * Sets up JNDI context configuration and starts the Tomcat server for testing.
     * <p>
     * This method performs the following operations:
     * </p>
     * <ol>
     * <li>Sets the application context path based on the passed contextPath
     * parameter</li>
     * <li>Generates the corresponding context name, handling the special case of
     * root path</li>
     * <li>Creates a temporary Tomcat context configuration file in the system
     * temporary directory</li>
     * <li>Writes JNDI resource configuration to the temporary configuration
     * file</li>
     * <li>Starts the embedded Tomcat server and configures the JNDI check
     * servlet</li>
     * </ol>
     * 
     * @param testContextPath the context path to use for testing. If null, the
     *                        default value "/" will be used
     * @throws Exception if any exception occurs during configuration, including
     *                   file creation failure, server startup failure, etc.
     */
    private void setupJndiContextXml(String testContextPath) throws Exception {
        // Set contextPath, use default value if null is passed
        if (testContextPath != null) {
            contextPath = testContextPath;
        } else {
            contextPath = "/"; // Default value
        }

        String contextName = contextPath;
        if (contextName.startsWith("/")) {
            contextName = contextName.substring(1);
        }
        if (contextName.isEmpty()) {
            contextName = "ROOT";
        }

        // Create temporary context XML file in system temp directory
        try {
            temporaryContextXml = File.createTempFile(contextName + "_context", ".xml");
            createTemporaryContextXml(temporaryContextXml);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary context XML file", e);
        }

        File contextXml = temporaryContextXml;

        Assert.assertTrue("Missing context XML: " + contextXml.getAbsolutePath(), contextXml.isFile());

        System.setProperty("jndi.contextFile", contextXml.getAbsolutePath());

        startTomcatServer(contextPath, true, new ServletConfigurer() {
            public void configure(org.apache.catalina.Context ctx) throws Exception {
                TomcatTestSupport.addJndiResourcesFromContextXml(ctx, contextXml);
                org.apache.catalina.startup.Tomcat.addServlet(ctx, "jndiCheck", new JndiCheckServlet());
                ctx.addServletMapping("/__jndi", "jndiCheck");
            }
        });
    }

    /**
     * Verifies that JNDI resources are working properly.
     * <p>
     * This method parses the response content from the JNDI check servlet and
     * verifies the following conditions:
     * </p>
     * <ul>
     * <li>jdbc/demo_db datasource status is OK or OK (connected)</li>
     * 
     * <li>Response contains no FAIL status</li>
     * </ul>
     * 
     * @param body the response content returned by the JNDI check servlet
     * @throws AssertionError if any JNDI resource verification fails
     */
    private void assertJndiResourcesWork(String body) {
        // We expect the resource to be bound as DataSource.
        Assert.assertTrue("Expected jdbc/demo_db OK. Body:\n" + body,
                body.contains("jdbc/demo_db=OK") || body.contains("jdbc/demo_db=OK (connected)"));

        Assert.assertFalse("Unexpected FAIL in JNDI check. Body:\n" + body, body.contains("=FAIL"));
    }

    /**
     * Creates a temporary context XML file containing JNDI resource configuration.
     * <p>
     * This method generates a standard Tomcat context configuration file containing
     * an H2 in-memory database datasource configuration:
     * </p>
     * <ul>
     * <li><strong>jdbc/demo_db</strong> - Primary data management system
     * datasource</li>
     * </ul>
     * <p>
     * The datasource is configured to use H2 in-memory database with connection
     * parameters including:
     * </p>
     * <ul>
     * <li>Driver class: org.h2.Driver</li>
     * <li>Maximum connections: 100</li>
     * <li>Maximum idle connections: 100</li>
     * <li>Maximum wait time: 100 seconds</li>
     * <li>Authentication method: Container</li>
     * </ul>
     * 
     * @param contextXml the context configuration file to create
     * @throws IOException if I/O exception occurs during file writing
     */
    private void createTemporaryContextXml(File contextXml) throws IOException {
        // Create the context XML with mock JNDI resource
        String contextXmlContent = "<Context reloadable=\"false\">\n" +
                "    <!-- Mock JNDI DataSource resource for testing using H2 in-memory database -->\n" +
                "    \n" +
                "    <!-- Mock database for a1_dms -->\n" +
                "    <Resource\n" +
                "            auth=\"Container\"\n" +
                "            driverClassName=\"org.h2.Driver\"\n" +
                "            maxTotal=\"100\"\n" +
                "            maxIdle=\"100\"\n" +
                "            maxWaitMillis=\"100000\"\n" +
                "            name=\"jdbc/demo_db\"\n" +
                "            password=\"\"\n" +
                "            type=\"javax.sql.DataSource\"\n" +
                "            url=\"jdbc:h2:mem:a1_dms;DB_CLOSE_DELAY=-1\"\n" +
                "            username=\"sa\"/>\n" +
                "</Context>\n";

        try (FileWriter writer = new FileWriter(contextXml)) {
            writer.write(contextXmlContent);
        }
    }
}
