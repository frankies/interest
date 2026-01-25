---
name: java_enterprise_development
description: 企业级Java开发技能。使用Spring Boot 3.x + Spring Cloud构建高并发微服务系统，严格遵守阿里巴巴Java开发手册和Google Java Style。支持完整分层架构、设计模式应用、分布式事务处理。代码覆盖率>80%，通过SonarQube检查。触发词：java、spring boot、微服务、rest api、service
---

# Java 企业级开发技能

## 能力范围
- Spring Boot 3.x + Spring Cloud 微服务开发
- 高并发系统设计（支持1000+ TPS）
- 分布式事务处理（Seata）
- 代码规范（阿里巴巴Java开发手册）
- 性能优化和可观测性

## 技术栈
- 框架：Spring Boot 3.1 + Spring Cloud 2022
- 数据库：MySQL 8.0 + Redis 7.0
- 消息队列：RabbitMQ
- ORM：MyBatis-Plus
- 文档：SpringDoc OpenAPI 3.0

## 设计模式
必须应用以下设计模式：
1. **DTO模式**：Controller与Service层解耦
2. **策略模式**：不同支付方式处理
3. **观察者模式**：订单状态变更通知
4. **建造者模式**：复杂对象创建
5. **工厂模式**：优惠券计算工厂
6. **熔断降级**：使用Resilience4j
7. **连接池**：HikariCP配置优化

## 代码结构标准
```
com.example.[module]
├── [Module]Application.java      # 启动类
├── config/                        # 配置类
├── controller/                    # REST接口
│   └── dto/                       # 请求/响应DTO
├── service/                       # 业务层
│   └── impl/                      # 实现类
├── domain/                        # 领域模型
│   ├── entity/                    # 实体类
│   └── enums/                     # 枚举类
├── repository/                    # 数据层
├── infrastructure/                # 基础设施
│   ├── mq/                        # 消息队列
│   └── cache/                     # 缓存服务
├── exception/                     # 异常处理
└── utils/                         # 工具类
```

## 非功能需求
- 并发处理：支持1000 TPS
- 响应时间：P99 < 200ms
- 数据一致性：分布式事务
- 可观测性：完整的Metrics、Logging、Tracing
- 代码覆盖率 > 80%

## 安全要求
1. SQL注入防护：全部使用参数化查询
2. XSS防护：响应数据转义
3. 敏感数据脱敏：手机号、邮箱
4. 权限校验：@PreAuthorize注解
