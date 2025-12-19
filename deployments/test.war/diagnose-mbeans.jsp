<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.management.*" %>
<%@ page import="java.lang.management.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>MBean Domains Diagnostic</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            max-width: 1200px;
        }
        . domain-section {
            margin:  20px 0;
            padding: 15px;
            background-color: #f9f9f9;
            border-left: 4px solid #2196F3;
        }
        .domain-name {
            font-size: 18px;
            font-weight: bold;
            color: #2196F3;
            margin-bottom: 10px;
        }
        .mbean-item {
            padding: 8px;
            margin: 3px 0;
            background-color: white;
            border:  1px solid #ddd;
            font-family: monospace;
            font-size: 11px;
        }
        . highlight {
            background-color: #fff59d;
            font-weight: bold;
        }
        .datasource-related {
            background-color: #c8e6c9;
            border-left: 4px solid #4CAF50;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>WildFly MBean Domains Diagnostic</h1>
        <p>WildFly Version: 10.0.1.Final</p>
        
<%
    try {
        // 获取 MBeanServer
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        
        // 查询所有 MBean
        Set<ObjectName> allMBeans = mbs.queryNames(null, null);
        
        // 按域名分组
        Map<String, List<ObjectName>> mbeansByDomain = new TreeMap<>();
        
        for (ObjectName objName : allMBeans) {
            String domain = objName. getDomain();
            if (!mbeansByDomain.containsKey(domain)) {
                mbeansByDomain.put(domain, new ArrayList<ObjectName>());
            }
            mbeansByDomain.get(domain).add(objName);
        }
        
        out.println("<h2>Total Domains Found: " + mbeansByDomain.size() + "</h2>");
        out.println("<h2>Total MBeans:  " + allMBeans.size() + "</h2>");
        
        out.println("<h3>All Available Domains:</h3>");
        out.println("<ul>");
        for (String domain : mbeansByDomain. keySet()) {
            out.println("<li><strong>" + domain + "</strong> (" + mbeansByDomain. get(domain).size() + " MBeans)</li>");
        }
        out.println("</ul>");
        
        out.println("<hr>");
        out.println("<h2>Searching for DataSource Related MBeans...</h2>");
        
        // 查找所有包含 datasource 关键字的 MBean
        boolean foundDataSource = false;
        for (Map.Entry<String, List<ObjectName>> entry : mbeansByDomain.entrySet()) {
            String domain = entry.getKey();
            List<ObjectName> mbeans = entry.getValue();
            
            List<ObjectName> dsRelated = new ArrayList<>();
            for (ObjectName objName : mbeans) {
                String fullName = objName.toString().toLowerCase();
                if (fullName.contains("datasource") || 
                    fullName.contains("data-source") ||
                    fullName.contains("pool") ||
                    fullName. contains("jdbc")) {
                    dsRelated.add(objName);
                }
            }
            
            if (! dsRelated.isEmpty()) {
                foundDataSource = true;
%>
        <div class="domain-section">
            <div class="domain-name">Domain: <%= domain %> (<%= dsRelated.size() %> DataSource-related MBeans)</div>
<%
                for (ObjectName objName : dsRelated) {
                    String cssClass = objName.toString().toLowerCase().contains("exampleds") ? 
                                    "mbean-item datasource-related" : "mbean-item";
%>
            <div class="<%= cssClass %>">
                <%= objName.toString() %>
                
<%
                    // 尝试获取属性
                    if (objName.toString().toLowerCase().contains("pool") || 
                        objName. toString().toLowerCase().contains("statistics")) {
                        try {
                            MBeanInfo mbeanInfo = mbs.getMBeanInfo(objName);
                            MBeanAttributeInfo[] attributes = mbeanInfo.getAttributes();
                            
                            out.println("<div style='margin-left: 20px; margin-top: 5px; font-size: 10px; color: #666;'>");
                            out.println("<strong>Attributes:</strong> ");
                            
                            List<String> attrNames = new ArrayList<>();
                            for (MBeanAttributeInfo attr : attributes) {
                                if (attr.isReadable()) {
                                    attrNames.add(attr.getName());
                                }
                            }
                            out.println(String.join(", ", attrNames));
                            out.println("</div>");
                            
                            // 如果找到 ActiveCount，特别标注
                            for (MBeanAttributeInfo attr : attributes) {
                                if (attr.getName().equals("ActiveCount") && attr.isReadable()) {
                                    try {
                                        Object value = mbs. getAttribute(objName, "ActiveCount");
                                        out.println("<div style='margin-left: 20px; color: #4CAF50; font-weight: bold;'>");
                                        out.println("✓ ActiveCount = " + value);
                                        out.println("</div>");
                                    } catch (Exception e) {
                                        out.println("<div style='margin-left: 20px; color: #f44336;'>");
                                        out. println("✗ ActiveCount:  " + e.getMessage());
                                        out.println("</div>");
                                    }
                                }
                            }
                            
                        } catch (Exception e) {
                            out.println("<div style='margin-left: 20px; color: #f44336; font-size: 10px;'>");
                            out.println("Error: " + e.getMessage());
                            out.println("</div>");
                        }
                    }
%>
            </div>
<%
                }
%>
        </div>
<%
            }
        }
        
        if (!foundDataSource) {
            out.println("<div style='color: #f44336; padding: 15px; background-color: #ffebee;'>");
            out.println("<strong>Warning:</strong> No DataSource-related MBeans found!");
            out.println("<p>Possible reasons:</p>");
            out.println("<ul>");
            out.println("<li>DataSource statistics are not enabled</li>");
            out.println("<li>No DataSource is configured</li>");
            out.println("<li>MBean registration is disabled</li>");
            out.println("</ul>");
            out.println("</div>");
        }
        
        // 显示所有 jboss 相关的域
        out.println("<hr>");
        out.println("<h2>JBoss Related Domains:</h2>");
        boolean foundJBoss = false;
        for (String domain : mbeansByDomain. keySet()) {
            if (domain.toLowerCase().contains("jboss") || 
                domain.toLowerCase().contains("wildfly") ||
                domain.toLowerCase().contains("undertow")) {
                foundJBoss = true;
                out.println("<div class='domain-section'>");
                out. println("<div class='domain-name'>" + domain + " (" + mbeansByDomain. get(domain).size() + " MBeans)</div>");
                
                List<ObjectName> mbeans = mbeansByDomain. get(domain);
                // 只显示前50个，避免页面过大
                int count = 0;
                for (ObjectName objName : mbeans) {
                    if (count++ > 50) {
                        out.println("<div class='mbean-item'>...  and " + (mbeans.size() - 50) + " more</div>");
                        break;
                    }
                    out. println("<div class='mbean-item'>" + objName.toString() + "</div>");
                }
                out.println("</div>");
            }
        }
        
        if (!foundJBoss) {
            out.println("<p style='color: #f44336;'>No JBoss/WildFly specific domains found!</p>");
        }
        
    } catch (Exception e) {
%>
        <div style="color: #d32f2f; padding: 10px; background-color: #ffebee;">
            <strong>Error:</strong> <%= e.getClass().getName() %><br>
            <strong>Message:</strong> <%= e. getMessage() %><br>
            <pre><% e.printStackTrace(new java.io.PrintWriter(out)); %></pre>
        </div>
<%
    }
%>
    </div>
</body>
</html>