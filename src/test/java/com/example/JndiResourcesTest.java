package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.utils.TomcatTestSupport;

public class JndiResourcesTest extends BaseTomcatTest {

    private static String contextPath;
    private static File temporaryContextXml;

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

    @AfterClass
    public static void cleanupTemporaryFiles() {
        // Clean up temporary context XML file if we created it
        if (temporaryContextXml != null && temporaryContextXml.exists()) {
            temporaryContextXml.delete();
        }
    }

    private static void createTemporaryContextXml(File contextXml) throws IOException {
        // Create the context XML with mock JNDI resources
        String contextXmlContent = "<Context reloadable=\"false\">\n" +
                "    <!-- Mock JNDI DataSource resources for testing using H2 in-memory database -->\n" +
                "    \n" +
                "    <!-- Mock database for a1_dms -->\n" +
                "    <Resource\n" +
                "            auth=\"Container\"\n" +
                "            driverClassName=\"org.h2.Driver\"\n" +
                "            maxTotal=\"100\"\n" +
                "            maxIdle=\"100\"\n" +
                "            maxWaitMillis=\"100000\"\n" +
                "            name=\"jdbc/a1_dms\"\n" +
                "            password=\"\"\n" +
                "            type=\"javax.sql.DataSource\"\n" +
                "            url=\"jdbc:h2:mem:a1_dms;DB_CLOSE_DELAY=-1\"\n" +
                "            username=\"sa\"/>\n" +
                "\n" +
                "    <!-- Mock database for a1_dms_cmmdb -->\n" +
                "    <Resource\n" +
                "            auth=\"Container\"\n" +
                "            driverClassName=\"org.h2.Driver\"\n" +
                "            maxTotal=\"100\"\n" +
                "            maxIdle=\"100\"\n" +
                "            maxWaitMillis=\"100000\"\n" +
                "            name=\"jdbc/a1_dms_cmmdb\"\n" +
                "            password=\"\"\n" +
                "            type=\"javax.sql.DataSource\"\n" +
                "            url=\"jdbc:h2:mem:a1_dms_cmmdb;DB_CLOSE_DELAY=-1\"\n" +
                "            username=\"sa\"/>\n" +
                "\n" +
                "    <!-- Mock database for a1dms_bi -->\n" +
                "    <Resource\n" +
                "            auth=\"Container\"\n" +
                "            driverClassName=\"org.h2.Driver\"\n" +
                "            maxTotal=\"100\"\n" +
                "            maxIdle=\"100\"\n" +
                "            maxWaitMillis=\"100000\"\n" +
                "            name=\"jdbc/a1dms_bi\"\n" +
                "            password=\"\"\n" +
                "            type=\"javax.sql.DataSource\"\n" +
                "            url=\"jdbc:h2:mem:a1dms_bi;DB_CLOSE_DELAY=-1\"\n" +
                "            username=\"sa\"/>\n" +
                "</Context>\n";

        try (FileWriter writer = new FileWriter(contextXml)) {
            writer.write(contextXmlContent);
        }
    }
}
