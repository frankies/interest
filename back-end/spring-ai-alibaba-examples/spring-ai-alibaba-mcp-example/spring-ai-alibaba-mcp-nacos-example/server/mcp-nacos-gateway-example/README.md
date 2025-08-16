# Spring AI MCP + Nacos Gateway 示例项目

本项目是一个基于 [spring-ai-alibaba-mcp-gateway-nacos](https://github.com/spring-projects/spring-ai-alibaba) 的简单示例，展示如何构建一个 MCP Gateway 服务，动态代理 Nacos 中注册的 MCP 服务。

本示例是 MCP Gateway 代理 Nacos 中的 MCP 服务，实现服务能力到 AI 工具的转化，要求版本如下：

1. Nacos 版本在 3.0.1 及以上
2. spring ai alibaba 的 1.0.0.3-SNAPSHOT 以上

## 🧩 主要依赖

```xml
<!-- MCP Gateway Nacos 支持 -->
<dependency>
    <groupId>com.alibaba.cloud.ai</groupId>
    <artifactId>spring-ai-alibaba-starter-mcp-gateway-nacos</artifactId>
    <version>${spring-ai-alibaba.version}</version>
</dependency>

<!-- MCP Server WebMvc 支持 -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webmvc</artifactId>
</dependency>
```

---

## 🚀 快速开始

### 1. 启动 Nacos 服务

请确保你已经本地或远程启动了 [Nacos 服务器], 要求 Nacos 版本>=3.0.1

### 2. 配置 application.yml

#### 基础配置

```yaml
spring:
  application:
    name: mcp-nacos-gateway-example
  ai:
    mcp:
      server:
        name: mcp-gateway-example
        version: 1.0.0
        type: SYNC
        instructions: "This gateway server provides dynamic MCP tools from Nacos registered services"

    alibaba:
      mcp:
        nacos:
          enabled: true
          server-addr: 127.0.0.1:8848
          namespace: 4ad3108b-4d44-43d0-9634-3c1ac4850c8c
          username: nacos
          password: nacos
        gateway:
          enabled: true
          registry: nacos
          sse:
            enabled: true # default is true
          streamable:
            enabled: true # default is false
          nacos:
            service-names:
              - webflux-mcp-server
              - webmvc-mcp-server

server:
  port: 8090
```

### 3. 启动应用

```bash
mvn spring-boot:run
```
