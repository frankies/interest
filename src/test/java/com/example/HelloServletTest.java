package com.example;

import org.apache.catalina.startup.Tomcat;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.base.BaseTomcatTest;
import com.example.utils.TomcatTestSupport;

import static org.junit.Assert.assertTrue;

/**
 * Integration test class for the HelloServlet.
 * <p>
 * This test class verifies the functionality of the HelloServlet by deploying
 * it in an embedded Tomcat server and making HTTP requests to test its
 * behavior.
 * The test ensures that the servlet generates the expected HTML content and
 * responds correctly to HTTP GET requests.
 * </p>
 * 
 * <h3>Test Coverage:</h3>
 * <ul>
 * <li>HTTP GET request handling</li>
 * <li>HTML content generation</li>
 * <li>Servlet deployment and mapping</li>
 * </ul>
 * 
 * @author Generated Test
 * @version 1.0
 * @since 1.0
 */
public class HelloServletTest extends BaseTomcatTest {

    /**
     * Sets up the test environment by starting an embedded Tomcat server.
     * <p>
     * This method runs once before all test methods in the class and configures
     * an embedded Tomcat server with the HelloServlet mapped to the "/hello" path.
     * JNDI is disabled for this test since the HelloServlet doesn't require
     * database connectivity.
     * </p>
     * 
     * @throws Exception if server setup or servlet configuration fails
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        startTomcatServer("", false, new ServletConfigurer() {
            public void configure(org.apache.catalina.Context ctx) throws Exception {
                Tomcat.addServlet(ctx, "helloServlet", new HelloServlet());
                ctx.addServletMapping("/hello", "helloServlet");
            }
        });
    }

    /**
     * Tests that the HelloServlet responds correctly to HTTP GET requests.
     * <p>
     * This test makes an HTTP GET request to the "/hello" endpoint and verifies
     * that the response contains the expected welcome message. This ensures that
     * the servlet is properly deployed, accessible via HTTP, and generating
     * the correct content.
     * </p>
     * 
     * @throws Exception if HTTP request fails or response is invalid
     */
    @Test
    public void testHelloServletOverHttp() throws Exception {
        String body = TomcatTestSupport.httpGetText(server.getPort(), "/hello");
        assertTrue(body.contains("Hello, Maven Web Project!"));
    }
}
