# Spring Cloud Gateway 微服务网关

基于 Spring Cloud Gateway 的企业级 API 网关，提供统一的微服务入口、认证授权、流量控制、熔断降级等功能。

## 功能特性

### 1. 用户认证与授权
- 基于 JWT 的无状态认证
- Spring Security 集成
- 支持角色和权限控制
- 自动将用户信息传递给下游服务

### 2. 请求路由与转发
- 基于服务名的动态路由（集成 Nacos 服务发现）
- 支持路径、方法、请求头等多条件路由
- 路径重写和请求增强
- 动态路由配置

### 3. 日志采集与链路追踪
- 全局请求/响应日志记录
- 自动生成 TraceId 用于链路追踪
- 集成 Zipkin 分布式追踪
- 结构化日志输出

### 4. 限流功能
- 基于 Redis 的分布式限流
- 支持按 IP、用户 ID 等维度限流
- 令牌桶算法实现
- 可配置限流策略

### 5. 负载均衡
- Spring Cloud LoadBalancer 客户端负载均衡
- 支持轮询、随机等策略
- 自动服务实例发现
- 异常实例剔除

### 6. 熔断与降级
- Resilience4j 熔断器集成
- 可配置熔断阈值和恢复策略
- 统一降级响应处理
- 超时控制

### 7. 分布式部署
- 无状态设计，支持横向扩展
- 容器化部署支持
- 配置中心集成（Nacos Config）
- 健康检查和监控端点

## 技术栈

- **Spring Boot 4.0.1** - 基础框架
- **Spring Cloud Gateway** - 网关核心
- **Spring Security** - 安全认证
- **JWT (JJWT 0.12.5)** - 令牌认证
- **Nacos** - 服务注册与配置中心
- **Redis** - 分布式限流
- **Resilience4j** - 熔断降级
- **Zipkin** - 链路追踪
- **Spring Boot Actuator** - 监控
- **Lombok** - 代码简化

## 快速开始

### 前置要求

- JDK 25+
- Gradle 9.2+
- Redis 服务器
- Nacos 服务器（可选）
- Zipkin 服务器（可选）

### 本地运行

1. 启动 Redis
```bash
docker run -d -p 6379:6379 redis:latest
```

2. 启动 Nacos（可选）
```bash
docker run -d -p 8848:8848 -e MODE=standalone nacos/nacos-server:latest
```

3. 启动 Zipkin（可选）
```bash
docker run -d -p 9411:9411 openzipkin/zipkin
```

4. 构建项目
```bash
./gradlew build
```

5. 运行网关
```bash
./gradlew bootRun
```

网关将在 `http://localhost:8080` 启动

### GraalVM Native Image

生成 Native Image：
```bash
./gradlew nativeCompile
```

运行 Native Image：
```bash
./gradlew nativeRun
```

## 配置说明

### 路由配置

在 `application.yaml` 中配置路由规则：

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
          filters:
            - StripPrefix=1
```

### JWT 配置

```yaml
spring:
  security:
    jwt:
      secret: your-secret-key
      expiration: 86400000
```

### 限流配置

```yaml
spring:
  cloud:
    gateway:
      routes:
        - filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
```

### 熔断配置

```yaml
resilience4j:
  circuitbreaker:
    instances:
      userServiceCircuitBreaker:
        sliding-window-size: 10
        failure-rate-threshold: 50
        wait-duration-in-open-state: 10000
```

## API 端点

### 健康检查
```
GET /actuator/health
```

### 监控指标
```
GET /actuator/metrics
GET /actuator/prometheus
```

### 降级端点
```
GET /fallback/{service}
```

## 项目结构

```
src/main/java/com/example/gateway/
├── GatewayApplication.java          # 主应用类
├── config/
│   ├── SecurityConfig.java          # 安全配置
│   ├── RateLimitConfig.java         # 限流配置
│   └── LoadBalancerConfig.java      # 负载均衡配置
├── filter/
│   ├── JwtAuthenticationFilter.java # JWT 认证过滤器
│   └── LoggingFilter.java           # 日志过滤器
├── controller/
│   └── FallbackController.java      # 降级控制器
├── exception/
│   └── GlobalExceptionHandler.java  # 全局异常处理
└── util/
    └── JwtUtil.java                 # JWT 工具类
```

## 部署

### Docker 部署

创建 Dockerfile：
```dockerfile
FROM eclipse-temurin:25-jre
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

构建镜像：
```bash
docker build -t gateway:latest .
```

运行容器：
```bash
docker run -d -p 8080:8080 gateway:latest
```

### Kubernetes 部署

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: gateway
spec:
  replicas: 3
  selector:
    matchLabels:
      app: gateway
  template:
    metadata:
      labels:
        app: gateway
    spec:
      containers:
      - name: gateway
        image: gateway:latest
        ports:
        - containerPort: 8080
```

## 监控与运维

- **健康检查**: `/actuator/health`
- **指标监控**: `/actuator/metrics`
- **链路追踪**: Zipkin UI `http://localhost:9411`
- **日志聚合**: 支持 ELK/Loki 集成

## 安全建议

1. 修改 JWT secret 为强密钥（至少 256 位）
2. 启用 HTTPS
3. 配置 CORS 策略
4. 定期更新依赖版本
5. 使用环境变量管理敏感配置
6. 启用 API 访问日志审计

## 性能优化

- 使用 Reactor 响应式编程模型
- Redis 连接池优化
- 合理配置熔断和超时参数
- 启用 HTTP/2
- 使用 GraalVM Native Image 提升启动速度

## 故障排查

查看日志：
```bash
./gradlew bootRun --debug
```

检查 Redis 连接：
```bash
redis-cli ping
```

检查 Nacos 服务注册：
```bash
curl http://localhost:8848/nacos/v1/ns/instance/list?serviceName=api-gateway
```

## 许可证

本项目基于 Apache License 2.0 开源协议
