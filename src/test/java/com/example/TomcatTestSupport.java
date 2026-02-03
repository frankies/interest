package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class TomcatTestSupport {

    private TomcatTestSupport() {
    }

    public static RunningTomcat start(String contextPath, boolean enableNaming, ContextConfigurer configure)
            throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(Files.createTempDirectory("tomcat-test-").toFile().getAbsolutePath());
        if (enableNaming) {
            tomcat.enableNaming();
        }

        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(0);
        tomcat.getService().addConnector(connector);
        tomcat.setConnector(connector);

        File docBase = Files.createTempDirectory("tomcat-test-docBase-").toFile();
        Context context = tomcat.addContext(contextPath, docBase.getAbsolutePath());

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

    public static String httpGetText(int port, String path) throws Exception {
        URL url = new URL("http://localhost:" + port + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10_000);
        conn.setReadTimeout(10_000);
        conn.setRequestMethod("GET");

        int code = conn.getResponseCode();
        String body;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                code >= 200 && code < 400 ? conn.getInputStream() : conn.getErrorStream(),
                StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            body = sb.toString();
        }

        if (code != 200) {
            throw new AssertionError("HTTP " + code + " for GET " + path + "\n" + body);
        }
        return body;
    }

    public static void addJndiResourcesFromContextXml(Context ctx, File xmlFile) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
        NodeList resources = doc.getElementsByTagName("Resource");

        for (int i = 0; i < resources.getLength(); i++) {
            Element el = (Element) resources.item(i);

            String name = el.getAttribute("name");
            if (name == null || name.trim().isEmpty()) {
                continue;
            }

            ContextResource cr = new ContextResource();
            cr.setName(name);
            cr.setType(attrOrDefault(el, "type", "javax.sql.DataSource"));
            cr.setAuth(attrOrDefault(el, "auth", "Container"));

            // Force a known factory for embedded Tomcat tests.
            cr.setProperty("factory", "org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory");

            copyIfPresent(el, cr, "driverClassName");
            copyIfPresent(el, cr, "url");
            copyIfPresent(el, cr, "username");
            copyIfPresent(el, cr, "password");
            copyIfPresent(el, cr, "maxTotal");
            copyIfPresent(el, cr, "maxIdle");
            copyIfPresent(el, cr, "maxWaitMillis");

            ctx.getNamingResources().addResource(cr);
        }
    }

    private static void copyIfPresent(Element el, ContextResource cr, String attr) {
        String v = el.getAttribute(attr);
        if (v != null && !v.trim().isEmpty()) {
            cr.setProperty(attr, v);
        }
    }

    private static String attrOrDefault(Element el, String attr, String def) {
        String v = el.getAttribute(attr);
        if (v == null || v.trim().isEmpty()) {
            return def;
        }
        return v;
    }

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

    public interface ContextConfigurer {
        void configure(Context ctx) throws Exception;
    }

    public static final class RunningTomcat implements AutoCloseable {
        private final Tomcat tomcat;
        private final int port;

        private RunningTomcat(Tomcat tomcat, int port) {
            this.tomcat = tomcat;
            this.port = port;
        }

        public int getPort() {
            return port;
        }

        @Override
        public void close() throws Exception {
            if (tomcat == null) {
                return;
            }
            try {
                tomcat.stop();
            } finally {
                tomcat.destroy();
            }
        }
    }
}
