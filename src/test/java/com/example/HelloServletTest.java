package com.example;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class HelloServletTest {

    private static TomcatTestSupport.RunningTomcat server;

    @AfterClass
    public static void tearDownClass() throws Exception {
        if (server == null) {
            return;
        }
        server.close();
        server = null;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        server = TomcatTestSupport.start("", false, ctx -> {
            Tomcat.addServlet(ctx, "helloServlet", new HelloServlet());
            ctx.addServletMappingDecoded("/hello", "helloServlet");
        });
    }

    @Test
    public void testHelloServletOverHttp() throws Exception {
        String body = TomcatTestSupport.httpGetText(server.getPort(), "/hello");
        assertTrue(body.contains("Hello, Maven Web Project!"));
    }
}
