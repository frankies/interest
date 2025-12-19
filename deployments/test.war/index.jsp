<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax. management.*" %>
<%@ page import="java.lang.management.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>DataSource Pool Statistics</title>
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
            max-width:  800px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin:  20px 0;
        }
        th, td {
            padding: 12px;
            text-align:  left;
            border-bottom:  1px solid #ddd;
        }
        th {
            background-color: #2196F3;
            color:  white;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .value {
            font-weight: bold;
            color: #2196F3;
        }
        .error {
            color: #d32f2f;
            padding: 10px;
            background-color: #ffebee;
            border-left:  4px solid #d32f2f;
        }
        .refresh-btn {
            background-color:  #2196F3;
            color:  white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 4px;
            display: inline-block;
            margin:  10px 0;
        }
        . refresh-btn:hover {
            background-color: #1976D2;
        }
    </style>
    <script>
        // 自动刷新（可选）
       setTimeout(function(){ location.reload(); }, 5000); // 每5秒刷新
    </script>
</head>
<body>
    <div class="container">
        <h1>DataSource Pool Statistics - ExampleDS</h1>
        
<%
    try {
        // 获取 MBeanServer
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        
        // 定义 MBean ObjectName
        ObjectName objectName = new ObjectName("jboss.as:subsystem=datasources,data-source=ExampleDS,statistics=pool");
        
        // 定义要获取的属性列表
        String[] attributes = {
            "ActiveCount",
            "AvailableCount",
            "AverageBlockingTime",
            "AverageCreationTime",
            "CreatedCount",
            "DestroyedCount",
            "MaxCreationTime",
            "MaxUsedCount",
            "MaxWaitCount",
            "TimedOut",
            "TotalBlockingTime",
            "TotalCreationTime"
        };
        
%>
        <table>
            <thead>
                <tr>
                    <th>Attribute</th>
                    <th>Value</th>
                </tr>
            </thead>
            <tbody>
<%
        // 遍历并获取每个属性
        for (String attrName : attributes) {
            try {
                Object value = mbs.getAttribute(objectName, attrName);
                String displayValue = (value != null) ? value.toString() : "N/A";
%>
                <tr>
                    <td><%= attrName %></td>
                    <td class="value"><%= displayValue %></td>
                </tr>
<%
            } catch (AttributeNotFoundException e) {
                // 如果属性不存在，跳过
%>
                <tr>
                    <td><%= attrName %></td>
                    <td style="color: #999;">Not Available</td>
                </tr>
<%
            }
        }
%>
            </tbody>
        </table>	
        
        <a href="index.jsp" class="refresh-btn">Refresh</a>
        
        <div style="color: #666; font-size:  12px; margin-top:  20px;">
            Last updated: <%= new java.util.Date() %>
        </div>
        
<%
  
    } catch (Exception e) {
%>
        <div class="error">
            <strong>Error:</strong> <%= e.getClass().getName() %><br>
            <strong>Message:</strong> <%= e.getMessage() %><br>
            <strong>Stack Trace:</strong>
            <pre><% e.printStackTrace(new java.io.PrintWriter(out)); %></pre>
        </div>
<%
    }
%>
    </div>
</body>
</html>