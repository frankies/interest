package com.example.base;

import org.apache.catalina.Context;
import org.junit.AfterClass;

import com.example.utils.TomcatTestSupport;

/**
 * Abstract base class for tests that require an embedded Tomcat server.
 * <p>
 * This class provides common functionality for integration tests that need
 * to run servlets in an embedded Tomcat environment. It manages the server
 * lifecycle and provides a standardized way to configure servlets and
 * other web components for testing.
 * </p>
 * 
 * <h3>Features:</h3>
 * <ul>
 * <li>Manages embedded Tomcat server lifecycle</li>
 * <li>Provides servlet configuration interface</li>
 * <li>Ensures proper cleanup after test execution</li>
 * <li>Supports JNDI configuration for database testing</li>
 * </ul>
 * 
 * <h3>Usage:</h3>
 * <p>
 * Extend this class and use the {@link #startTomcatServer} method in
 * your test setup to configure and start the server with your test servlets.
 * </p>
 * 
 * @author Generated Test Base
 * @version 1.0
 * @since 1.0
 */
public abstract class BaseTomcatTest {

    protected static TomcatTestSupport.RunningTomcat server;
    private static boolean tearDownCalled = false;

    /**
     * Functional interface for configuring servlets and web components.
     * <p>
     * This interface allows test classes to provide custom configuration
     * logic for setting up servlets, filters, and other web components
     * in the embedded Tomcat context.
     * </p>
     */
    protected interface ServletConfigurer {
        /**
         * Configures the Tomcat context with servlets and other web components.
         * 
         * @param ctx the Tomcat context to configure
         * @throws Exception if configuration fails
         */
        void configure(Context ctx) throws Exception;
    }

    /**
     * Starts an embedded Tomcat server for testing.
     * <p>
     * This method creates and configures a new embedded Tomcat server instance
     * with the specified context path and optional JNDI support. The server
     * is configured using the provided ServletConfigurer implementation.
     * </p>
     * 
     * @param contextPath the web application context path
     * @param enableJndi  whether to enable JNDI naming support
     * @param configurer  the configurer to set up servlets and components
     * @throws Exception if server startup or configuration fails
     */
    protected static void startTomcatServer(String contextPath, boolean enableJndi, ServletConfigurer configurer)
            throws Exception {
        server = TomcatTestSupport.start(contextPath, enableJndi, new TomcatTestSupport.ContextConfigurer() {
            public void configure(Context ctx) throws Exception {
                configurer.configure(ctx);
            }
        });
    }

    /**
     * Shuts down the embedded Tomcat server and cleans up resources.
     * <p>
     * This method is called automatically after all tests in the class
     * have completed. It ensures proper cleanup of the Tomcat server
     * instance and prevents resource leaks. The method is idempotent
     * and can be called multiple times safely.
     * </p>
     * 
     * @throws Exception if server shutdown fails, but exceptions are ignored
     */
    @AfterClass
    public static void tearDownServer() throws Exception {
        if (tearDownCalled) {
            return;
        }
        tearDownCalled = true;
        if (server == null) {
            return;
        }
        try {
            server.close();
        } catch (Exception ignored) {
        }
        server = null;
    }
}
