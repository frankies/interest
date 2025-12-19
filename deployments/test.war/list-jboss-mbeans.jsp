<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.management.*" %>
<%@ page import="java.lang.management.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>JBoss MBeans</title>
    <style>
        body { font-family: monospace; margin: 20px; }
        .mbean { padding: 10px; margin: 5px 0; background-color: #f5f5f5; border-left: 3px solid #4CAF50; }
        .domain { font-weight: bold; color: #2196F3; margin-top: 20px; }
    </style>
</head>
<body>
    <h1>JBoss MBeans</h1>
<%
    try {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        
        // 只查询 jboss.as 域的 MBean
        ObjectName pattern = new ObjectName("jboss.as:*");
        Set<ObjectName> jbossMBeans = mbs.queryNames(pattern, null);
        
        out.println("<p>Found " + jbossMBeans.size() + " MBeans in jboss.as domain</p>");
        
        // 查找包含 datasources 的 MBean
        out.println("<div class='domain'>DataSource Related MBeans:</div>");
        for (ObjectName objName : jbossMBeans) {
            if (objName.toString().toLowerCase().contains("datasource")) {
                out.println("<div class='mbean'>" + objName.toString() + "</div>");
                
                // 如果是 statistics=pool，显示其属性
                if (objName. toString().contains("statistics=pool")) {
                    try {
                        MBeanInfo info = mbs.getMBeanInfo(objName);
                        MBeanAttributeInfo[] attrs = info.getAttributes();
                        out.println("<div style='margin-left: 20px; font-size: 12px;'>");
                        out.println("<strong>Attributes:</strong><br>");
                        for (MBeanAttributeInfo attr : attrs) {
                            if (attr.isReadable()) {
                                try {
                                    Object value = mbs.getAttribute(objName, attr.getName());
                                    out.println(attr.getName() + " = " + value + "<br>");
                                } catch (Exception e) {
                                    out.println(attr.getName() + " = [Error:  " + e.getMessage() + "]<br>");
                                }
                            }
                        }
                        out.println("</div>");
                    } catch (Exception e) {
                        out.println("<div style='color: red;'>Error:  " + e.getMessage() + "</div>");
                    }
                }
            }
        }
        
    } catch (Exception e) {
        out.println("<div style='color: red;'>Error: " + e.getMessage() + "</div>");
        e.printStackTrace(new java.io.PrintWriter(out));
    }
%>
</body>
</html>