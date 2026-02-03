package com.example;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;

public class HelloServletTest {

    private static Tomcat tomcat;
    private static int port;

    private static synchronized void ensureStarted() throws Exception {
        if (tomcat != null) {
            return;
        }

        Path docBase = Files.createTempDirectory("simple-maven-pj-tomcat");
        docBase.toFile().deleteOnExit();

        tomcat = new Tomcat();
        tomcat.setBaseDir(docBase.toAbsolutePath().toString());
        tomcat.setPort(0);

        Context context = tomcat.addContext("", docBase.toAbsolutePath().toString());
        Tomcat.addServlet(context, "helloServlet", new HelloServlet());
        context.addServletMappingDecoded("/hello", "helloServlet");

        tomcat.getConnector();
        tomcat.start();
        port = tomcat.getConnector().getLocalPort();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        if (tomcat == null) {
            return;
        }
        try {
            tomcat.stop();
        } finally {
            tomcat.destroy();
            tomcat = null;
        }
    }

    @Test
    public void testHelloServletOverHttp() throws Exception {
        ensureStarted();

        URL url = new URL("http://localhost:" + port + "/hello");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10_000);
        connection.setReadTimeout(10_000);

        int code = connection.getResponseCode();
        assertEquals(200, code);

        String body;
        try (InputStream in = connection.getInputStream()) {
            body = readAllUtf8(in);
        }

        assertTrue(body.contains("Hello, Maven Web Project!"));
    }

    private static String readAllUtf8(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }
}
