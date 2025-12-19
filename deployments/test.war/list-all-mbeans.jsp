<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.management.*" %>
<%@ page import="java.lang.management.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>All MBeans List</title>
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
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
        }
        .search-box {
            margin:  20px 0;
            padding: 10px;
            background-color: #f9f9f9;
            border-radius: 4px;
        }
        .search-box input {
            width: 100%;
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .mbean-domain {
            margin: 20px 0;
            border:  1px solid #ddd;
            border-radius: 4px;
        }
        .domain-header {
            background-color: #2196F3;
            color: white;
            padding: 10px 15px;
            font-weight: bold;
            cursor: pointer;
        }
        .domain-header:hover {
            background-color: #1976D2;
        }
        .mbean-list {
            padding: 10px;
        }
        . mbean-item {
            padding: 10px;
            margin: 5px 0;
            background-color: #f5f5f5;
            border-left: 3px solid #4CAF50;
            font-family: monospace;
            font-size: 12px;
        }
        . mbean-item:hover {
            background-color: #e8f5e9;
            cursor: pointer;
        }
        .attributes {
            margin-top: 10px;
            padding: 10px;
            background-color: #fff;
            border:  1px solid #ddd;
            display: none;
        }
        .attribute-item {
            padding: 5px;
            border-bottom: 1px solid #eee;
        }
        .attribute-name {
            font-weight: bold;
            color: #1976D2;
            display: inline-block;
            width: 200px;
        }
        .attribute-value {
            color: #666;
        }
        .stats {
            background-color: #e3f2fd;
            padding:  15px;
            border-radius:  4px;
            margin-bottom: 20px;
        }
        .highlight {
            background-color: yellow;
        }
        .collapsible {
            display: none;
        }
        .expanded {
            display: block;
        }
    </style>
    <script>
        function filterMBeans() {
            var input = document.getElementById('searchInput');
            var filter = input.value.toLowerCase();
            var items = document.getElementsByClassName('mbean-item');
            
            for (var i = 0; i < items.length; i++) {
                var text = items[i].textContent || items[i].innerText;
                if (text.toLowerCase().indexOf(filter) > -1) {
                    items[i].style.display = "";
                } else {
                    items[i].style.display = "none";
                }
            }
        }
        
        function toggleDomain(domainId) {
            var element = document.getElementById(domainId);
            if (element.classList.contains('expanded')) {
                element.classList.remove('expanded');
            } else {
                element.classList.add('expanded');
            }
        }
        
        function toggleAttributes(id) {
            var element = document.getElementById(id);
            if (element.style.display === 'none' || element.style.display === '') {
                element.style.display = 'block';
            } else {
                element.style. display = 'none';
            }
        }
    </script>
</head>
<body>
    <div class="container">
        <h1>All MBeans Browser</h1>
        
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
        
        // 显示统计信息
%>
        <div class="stats">
            <strong>Total MBeans:</strong> <%= allMBeans.size() %><br>
            <strong>Total Domains:</strong> <%= mbeansByDomain.size() %><br>
            <strong>Server Time:</strong> <%= new java.util.Date() %>
        </div>
        
        <div class="search-box">
            <input type="text" id="searchInput" onkeyup="filterMBeans()" 
                   placeholder="Search MBeans...  (e.g., datasources, ActiveCount, jboss.as)">
        </div>
        
<%
        // 遍历每个域
        int domainIndex = 0;
        for (Map.Entry<String, List<ObjectName>> entry : mbeansByDomain.entrySet()) {
            String domain = entry.getKey();
            List<ObjectName> mbeans = entry.getValue();
            Collections.sort(mbeans, new Comparator<ObjectName>() {
                public int compare(ObjectName o1, ObjectName o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });
            
            String domainId = "domain_" + domainIndex;
%>
        <div class="mbean-domain">
            <div class="domain-header" onclick="toggleDomain('<%= domainId %>')">
                <%= domain %> (<%= mbeans.size() %> MBeans)
            </div>
            <div id="<%= domainId %>" class="mbean-list collapsible">
<%
            int mbeanIndex = 0;
            for (ObjectName objName : mbeans) {
                String attrId = "attr_" + domainIndex + "_" + mbeanIndex;
%>
                <div class="mbean-item" onclick="toggleAttributes('<%= attrId %>')">
                    <%= objName.toString() %>
                    
                    <div id="<%= attrId %>" class="attributes">
                        <strong>Attributes:</strong><br>
<%
                try {
                    MBeanInfo mbeanInfo = mbs.getMBeanInfo(objName);
                    MBeanAttributeInfo[] attributes = mbeanInfo.getAttributes();
                    
                    for (MBeanAttributeInfo attr : attributes) {
                        if (attr.isReadable()) {
                            try {
                                Object value = mbs.getAttribute(objName, attr.getName());
                                String displayValue = (value != null) ? value.toString() : "null";
                                
                                // 限制显示长度
                                if (displayValue.length() > 100) {
                                    displayValue = displayValue.substring(0, 100) + "...";
                                }
%>
                        <div class="attribute-item">
                            <span class="attribute-name"><%= attr.getName() %></span>
                            <span class="attribute-value">= <%= displayValue %></span>
                            <span style="color: #999; font-size: 11px;">(Type: <%= attr.getType() %>)</span>
                        </div>
<%
                            } catch (Exception e) {
%>
                        <div class="attribute-item">
                            <span class="attribute-name"><%= attr.getName() %></span>
                            <span style="color: #d32f2f;">Error: <%= e.getMessage() %></span>
                        </div>
<%
                            }
                        }
                    }
                } catch (Exception e) {
%>
                        <div style="color: #d32f2f;">Error getting attributes: <%= e.getMessage() %></div>
<%
                }
%>
                    </div>
                </div>
<%
                mbeanIndex++;
            }
%>
            </div>
        </div>
<%
            domainIndex++;
        }
%>
        
        <div style="margin-top: 20px; padding: 10px; background-color: #f9f9f9; border-radius: 4px;">
            <strong>Usage: </strong>
            <ul>
                <li>Click on a domain header to expand/collapse MBeans in that domain</li>
                <li>Click on an MBean to view its attributes and values</li>
                <li>Use the search box to filter MBeans</li>
            </ul>
        </div>
        
<%
    } catch (Exception e) {
%>
        <div style="color: #d32f2f; padding: 10px; background-color: #ffebee; border-left: 4px solid #d32f2f;">
            <strong>Error:</strong> <%= e.getClass().getName() %><br>
            <strong>Message:</strong> <%= e.getMessage() %><br>
            <pre><% e.printStackTrace(new java.io.PrintWriter(out)); %></pre>
        </div>
<%
    }
%>
    </div>
</body>
</html>