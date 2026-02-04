package com.example.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.deploy.NamingResources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class TomcatTestSupport {

    private TomcatTestSupport() {
    }

    public static RunningTomcat start(String contextPath, boolean enableNaming, ContextConfigurer configure)
            throws Exception {
        Tomcat tomcat = new Tomcat();
        File tempDir = File.createTempFile("tomcat-test-", "");
        tempDir.delete();
        tempDir.mkdir();
        tomcat.setBaseDir(tempDir.getAbsolutePath());
        if (enableNaming) {
            tomcat.enableNaming();
        }

        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(0);
        tomcat.getService().addConnector(connector);
        tomcat.setConnector(connector);

        File docBase = File.createTempFile("tomcat-test-docBase-", "");
        docBase.delete();
        docBase.mkdir();
        String adjustedPath = contextPath;
        if ("/".equals(contextPath)) {
            adjustedPath = "";
        }
        Context context = tomcat.addContext(adjustedPath, docBase.getAbsolutePath());

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
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestMethod("GET");

        int code = conn.getResponseCode();
        String body;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    code >= 200 && code < 400 ? conn.getInputStream() : conn.getErrorStream(),
                    "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            body = sb.toString();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {
                }
            }
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

            // Use Context resource setting directly instead of ContextResource
            try {
                String type = attrOrDefault(el, "type", "javax.sql.DataSource");
                String auth = attrOrDefault(el, "auth", "Container");

                // Create resource directly using Context methods
                NamingResources namingRes = ctx.getNamingResources();
                org.apache.catalina.deploy.ContextResource cr = new org.apache.catalina.deploy.ContextResource();
                cr.setName(name);
                cr.setType(type);
                cr.setAuth(auth);

                // Force a known factory for embedded Tomcat tests.
                cr.setProperty("factory", "org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory");

                copyIfPresent(el, cr, "driverClassName");
                copyIfPresent(el, cr, "url");
                copyIfPresent(el, cr, "username");
                copyIfPresent(el, cr, "password");
                copyIfPresent(el, cr, "maxTotal");
                copyIfPresent(el, cr, "maxIdle");
                // Tomcat 7 DBCP uses different property names
                copyIfPresent(el, cr, "maxActive");
                copyIfPresent(el, cr, "maxWait");

                namingRes.addResource(cr);
            } catch (Exception e) {
                // Log and continue with other resources if one fails
                System.err.println("Failed to add JNDI resource '" + name + "': " + e.getMessage());
            }
        }
    }

    private static void copyIfPresent(Element el, org.apache.catalina.deploy.ContextResource cr, String attr) {
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

    public static final class RunningTomcat {
        private final Tomcat tomcat;
        private final int port;
        private boolean closed = false;

        private RunningTomcat(Tomcat tomcat, int port) {
            this.tomcat = tomcat;
            this.port = port;
        }

        public int getPort() {
            return port;
        }

        public void close() throws Exception {
            if (tomcat == null || closed) {
                closed = true;
                return;
            }
            closed = true;
            try {
                tomcat.stop();
            } catch (Exception ignored) {
            }
            try {
                tomcat.destroy();
            } catch (Exception ignored) {
            }
        }
    }
}
