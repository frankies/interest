package com.example;

import org.apache.catalina.startup.Tomcat;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.utils.TomcatTestSupport;

import static org.junit.Assert.assertTrue;

public class HelloServletTest extends BaseTomcatTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        startTomcatServer("", false, new ServletConfigurer() {
            public void configure(org.apache.catalina.Context ctx) throws Exception {
                Tomcat.addServlet(ctx, "helloServlet", new HelloServlet());
                ctx.addServletMapping("/hello", "helloServlet");
            }
        });
    }

    @Test
    public void testHelloServletOverHttp() throws Exception {
        String body = TomcatTestSupport.httpGetText(server.getPort(), "/hello");
        assertTrue(body.contains("Hello, Maven Web Project!"));
    }
}
