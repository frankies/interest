package com.example;

import java.io.File;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.utils.TomcatTestSupport;

public class JndiResourcesTest extends BaseTomcatTest {

    private static String contextPath;

    @BeforeClass
    public static void startTomcatWithJndi() throws Exception {
        contextPath = System.getProperty("app.contextPath", "/");
        String contextName = contextPath;
        if (contextName.startsWith("/")) {
            contextName = contextName.substring(1);
        }
        if (contextName.isEmpty()) {
            contextName = "ROOT";
        }

        File contextXml = new File(".vscode/tomcat/Catalina/localhost/" + contextName + ".xml");
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

    @Test
    public void shouldLookupAllJndiResources() throws Exception {
        String body = TomcatTestSupport.httpGetText(server.getPort(), contextPath + "/__jndi");

        // We expect at least these resources, and we expect them to be bound as
        // DataSource.
        Assert.assertTrue("Expected jdbc/a1_dms OK. Body:\n" + body,
                body.contains("jdbc/a1_dms=OK") || body.contains("jdbc/a1_dms=OK (connected)"));
        Assert.assertTrue("Expected jdbc/a1_dms_cmmdb OK. Body:\n" + body,
                body.contains("jdbc/a1_dms_cmmdb=OK") || body.contains("jdbc/a1_dms_cmmdb=OK (connected)"));
        Assert.assertTrue("Expected jdbc/a1dms_bi OK. Body:\n" + body,
                body.contains("jdbc/a1dms_bi=OK") || body.contains("jdbc/a1dms_bi=OK (connected)"));

        Assert.assertFalse("Unexpected FAIL in JNDI check. Body:\n" + body, body.contains("=FAIL"));
    }
}
