package com.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class JndiCheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/plain; charset=UTF-8");

        boolean checkConnection = Boolean.parseBoolean(req.getParameter("connect"));
        if (!checkConnection) {
            checkConnection = Boolean.parseBoolean(System.getProperty("test.jndi.connect", "false"));
        }

        try {
            String[] jndiNames = getJndiNames();

            PrintWriter out = resp.getWriter();
            InitialContext ic = new InitialContext();

            resp.setStatus(200);
            for (String name : jndiNames) {
                String fullName = "java:comp/env/" + name;
                try {
                    Object obj = ic.lookup(fullName);
                    if (!(obj instanceof DataSource)) {
                        out.println(name + "=NOT_A_DATASOURCE (" + (obj == null ? "null" : obj.getClass().getName())
                                + ")");
                        continue;
                    }

                    if (!checkConnection) {
                        out.println(name + "=OK");
                        continue;
                    }

                    DataSource ds = (DataSource) obj;
                    try (Connection c = ds.getConnection()) {
                        out.println(name + "=OK (connected)");
                    }
                } catch (Exception e) {
                    out.println(name + "=FAIL (" + e.getClass().getSimpleName() + ": " + safeMessage(e) + ")");
                }
            }
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().println("Servlet failure: " + e.getClass().getSimpleName() + ": " + safeMessage(e));
        }
    }

    private static String[] getJndiNames() throws Exception {
        String contextFile = System.getProperty("jndi.contextFile", "").trim();
        if (!contextFile.isEmpty()) {
            File f = new File(contextFile);
            if (f.isFile()) {
                return readJndiNamesFromContextXml(f);
            }
        }

        return new String[] { "jdbc/a1_dms", "jdbc/a1_dms_cmmdb", "jdbc/a1dms_bi" };
    }

    private static String[] readJndiNamesFromContextXml(File xmlFile) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
        NodeList resources = doc.getElementsByTagName("Resource");
        String[] names = new String[resources.getLength()];
        for (int i = 0; i < resources.getLength(); i++) {
            Element el = (Element) resources.item(i);
            names[i] = el.getAttribute("name");
        }
        return names;
    }

    private static String safeMessage(Throwable t) {
        String m = t.getMessage();
        if (m == null) {
            return "";
        }
        return m.replace('\r', ' ').replace('\n', ' ');
    }
}
