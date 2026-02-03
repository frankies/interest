package com.example;

import java.io.File;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class JndiResourcesTest {

    private static TomcatTestSupport.RunningTomcat server;

    @BeforeClass
    public static void startTomcatWithJndi() throws Exception {
        File contextXml = new File(".vscode/tomcat/Catalina/localhost/a1stream.xml");
        Assert.assertTrue("Missing context XML: " + contextXml.getAbsolutePath(), contextXml.isFile());

        System.setProperty("jndi.contextFile", contextXml.getAbsolutePath());

        server = TomcatTestSupport.start("/a1stream", true, ctx -> {
            TomcatTestSupport.addJndiResourcesFromContextXml(ctx, contextXml);

            org.apache.catalina.startup.Tomcat.addServlet(ctx, "jndiCheck", new JndiCheckServlet());
            ctx.addServletMappingDecoded("/__jndi", "jndiCheck");
        });
    }

    @AfterClass
    public static void stopTomcat() throws Exception {
        if (server == null) {
            return;
        }
        server.close();
        server = null;
    }

    @Test
    public void shouldLookupAllJndiResources() throws Exception {
        String body = TomcatTestSupport.httpGetText(server.getPort(), "/a1stream/__jndi");

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
