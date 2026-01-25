# 包含Code Review和Java代码规范的Prompt模板

这里提供一个**完整的企业级Java开发Prompt模板**，包含**需求实现 + 代码规范 + Code Review**三部分。

## 完整Prompt模板

```
# 第一部分：开发任务描述
## CONTEXT
我们正在开发一个电商系统的订单服务模块，需要实现高并发的订单处理系统。这是生产环境代码，将服务于百万级用户。

## ROLE
你是：资深Java架构师 + Google Java代码规范专家 + 静态代码分析专家

## INSTRUCTION
请用Java 17 + Spring Boot 3.1实现一个完整的订单服务模块，具体要求：

### 1. 业务功能需求
- 订单创建（支持优惠券、库存校验）
- 订单状态机（待支付、已支付、已发货、已完成、已取消）
- 订单查询（分页、多条件筛选）
- 订单超时自动取消（30分钟未支付）
- 订单统计报表

### 2. 非功能需求
- 并发处理：支持1000 TPS
- 响应时间：P99 < 200ms
- 数据一致性：分布式事务（使用Seata）
- 可观测性：完整的Metrics、Logging、Tracing

## SCOPES & CONSTRAINTS
### 技术栈
- 框架：Spring Boot 3.1 + Spring Cloud 2022
- 数据库：MySQL 8.0 + Redis 7.0
- 消息队列：RabbitMQ
- ORM：MyBatis-Plus
- 文档：SpringDoc OpenAPI 3.0

### 代码规范
- 严格遵守《阿里巴巴Java开发手册》v1.7.0
- 通过SonarQube所有关键规则检查
- 代码覆盖率 > 80%
- 包含完整的Javadoc

# 第二部分：代码实现要求
## CODE STRUCTURE
请按以下包结构组织：

```
com.example.order

├── OrderApplication.java          # 启动类

├── config/                       # 配置类

│   ├── RedisConfig.java

│   ├── MybatisPlusConfig.java

│   └── ThreadPoolConfig.java

├── controller/                   # REST接口

│   ├── OrderController.java

│   └── dto/                      # 请求/响应DTO

├── service/                      # 业务层

│   ├── OrderService.java

│   ├── impl/OrderServiceImpl.java

│   └── task/OrderTimeoutTask.java

├── domain/                       # 领域模型

│   ├── entity/Order.java

│   ├── entity/OrderItem.java

│   └── enums/OrderStatus.java

├── repository/                   # 数据层

│   ├── OrderMapper.java

│   └── OrderRepository.java

├── infrastructure/               # 基础设施

│   ├── mq/OrderMessageSender.java

│   └── cache/OrderCacheService.java

├── exception/                    # 异常处理

│   ├── BusinessException.java

│   └── GlobalExceptionHandler.java

└── utils/                        # 工具类

└── IdGenerator.java

```
## PATTERNS TO APPLY
必须应用以下设计模式/最佳实践：
1. **DTO模式**：Controller与Service层解耦
2. **策略模式**：不同支付方式处理
3. **观察者模式**：订单状态变更通知
4. **建造者模式**：复杂对象创建
5. **工厂模式**：优惠券计算工厂
6. **熔断降级**：使用Resilience4j
7. **连接池**：HikariCP配置优化

## SECURITY REQUIREMENTS
1. SQL注入防护：全部使用参数化查询
2. XSS防护：响应数据转义
3. 敏感数据脱敏：手机号、邮箱
4. 权限校验：@PreAuthorize注解

# 第三部分：自动Code Review检查项
## CODE QUALITY RULES
请在你的代码中**主动标记**以下检查点：

### 1. 命名规范检查 ✔️/❌
```

java

// ✅ 正确示例

public interface OrderRepository extends JpaRepository<Order, Long> {

List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

}

// ❌ 错误示例

public interface orderDao {  // 接口名应为大驼峰

List<Order> find(Long uid, Integer st);  // 参数名不清晰

}

```
### 2. 代码风格检查 ✔️/❌
```

java

// ✅ 正确：Google Java Style

public class OrderService {

private static final int MAX_RETRY_COUNT = 3;

```
public Order createOrder(CreateOrderRequest request) {
    // 方法行数不超过80行
    // 嵌套不超过3层
}
```

}

// ❌ 错误

public class orderService {  // 类名应为大驼峰

private int maxRetryCount = 3;  // 常量应使用大写+下划线

}

```
### 3. 性能优化检查 ✔️/❌
```

java

// ✅ 正确

@Service

@RequiredArgsConstructor

public class OrderServiceImpl implements OrderService {

private final OrderRepository orderRepository;  // 构造器注入

private final RedisTemplate<String, Object> redisTemplate;

```
@Cacheable(value = "order", key = "#orderId")
public Order getOrderById(Long orderId) {
    return orderRepository.findById(orderId)
            .orElseThrow(() -> new BusinessException("订单不存在"));
}
```

}

// ❌ 错误

@Service

public class OrderServiceImpl implements OrderService {

@Autowired  // 字段注入，不推荐

private OrderRepository orderRepository;

```
public Order getOrderById(Long orderId) {
    // 缺少缓存，每次查询数据库
    return orderRepository.findOne(orderId);  // 方法名不规范
}
```

}

```
### 4. 并发安全检查 ✔️/❌
```

java

// ✅ 正确：线程安全的单例

@Component

public class IdGenerator {

private static final AtomicLong SEQUENCE = new AtomicLong(0);

```
public long nextId() {
    return SEQUENCE.incrementAndGet();
}
```

}

// ❌ 错误：非线程安全

public class IdGenerator {

private long sequence = 0;  // 非原子操作

```
public long nextId() {
    return ++sequence;  // 多线程下可能重复
}
```

}

```
### 5. 资源管理检查 ✔️/❌
```

java

// ✅ 正确：使用try-with-resources

public String readTemplate(String filePath) {

try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

return br.lines().collect(Collectors.joining("\n"));

} catch (IOException e) {

throw new BusinessException("读取模板失败", e);

}

}

// ❌ 错误：资源未关闭

public String readTemplate(String filePath) throws IOException {

BufferedReader br = new BufferedReader(new FileReader(filePath));

return br.readLine();  // 异常时资源泄漏

}

```
# 第四部分：输出格式要求
## 代码输出格式
请按以下格式输出：
```

## 1. 架构设计说明

[用Mermaid图展示架构]

## 2. 核心代码实现

### 2.1 Order.java (实体类)

```
// 代码实现...
// [Code Review] 这里使用了@Table注解，符合JPA规范 ✔️
// [Warning] 注意：BigDecimal字段应设置精度和刻度
```

### 2.2 OrderService.java (服务类)

```
// 代码实现...
// [Best Practice] 使用@Transactional确保事务一致性 ✔️
// [Performance] 添加了二级缓存，减少DB压力 ✔️
```

## 3. 代码规范自查报告

### 命名规范检查

- 

  [✔️] 类名使用大驼峰：OrderService

- 

  [✔️] 方法名使用小驼峰：createOrder

- 

  [✔️] 常量全大写：MAX_TIMEOUT_MINUTES

- 

  [✔️] 布尔类型以is/has开头：isPaid

### 代码质量检查

- 

  [✔️] 单个方法行数 < 80行

- 

  [✔️] 圈复杂度 < 10

- 

  [✔️] 重复代码 < 5%

- 

  [✔️] 异常处理完整

### 安全隐患检查

- 

  [✔️] 无SQL拼接

- 

  [✔️] 输入参数校验

- 

  [✔️] 日志脱敏处理

- 

  [✔️] 防重放攻击

## 4. 单元测试示例

```
@SpringBootTest
class OrderServiceTest {
    // 测试代码...
}
```

## 5. API文档 (OpenAPI 3.0)

```
openapi: 3.0.0
# API文档...
```

## 6. 部署配置

### application.yml

```
# 配置...
```

### Dockerfile

```
# Docker配置...
# 第五部分：扩展要求
## STATIC ANALYSIS PASS
生成的代码必须通过以下工具检查：
1. **SonarQube**：无Blocker/Critical级别问题
2. **Checkstyle**：符合Google Java Style
3. **SpotBugs**：无高危警告
4. **PMD**：代码质量规则通过

## PERFORMANCE OPTIMIZATION
1. 数据库：索引设计、查询优化
2. 缓存：多级缓存策略
3. 线程池：合理配置参数
4. JVM：内存参数建议

## MONITORING & LOGGING
1. 关键链路日志埋点
2. Metrics指标暴露
3. 慢查询监控
4. 异常报警规则
```

------

## 简化版Code Review专用Prompt

如果只需要对现有代码进行Review：

```
# CONTEXT
请对我的Java代码进行全面的Code Review

# ROLE
你是：拥有10年经验的Java技术专家 + 团队Code Review负责人

# INSTRUCTION
请从以下维度评审这段代码：

## 1. 代码规范检查
- 阿里巴巴Java开发手册合规性
- 命名规范（类、方法、变量、常量）
- 注释规范（类注释、方法注释、行注释）

## 2. 代码质量检查
- 单一职责原则
- 开闭原则
- 圈复杂度
- 重复代码
- 方法长度

## 3. 性能问题检查
- 内存泄漏风险
- 不必要的对象创建
- 循环中的耗时操作
- 缓存使用合理性
- 数据库查询优化

## 4. 并发安全检查
- 线程安全问题
- 锁的使用
- 原子性保证
- 死锁风险

## 5. 安全隐患检查
- SQL注入风险
- XSS漏洞
- 敏感信息泄露
- 权限校验缺失

## 6. 可维护性检查
- 代码可读性
- 模块耦合度
- 异常处理完整性
- 日志记录合理性

# FORMAT
请按以下格式输出评审结果：
```

## 代码评审报告

**文件**: [文件名]

**提交人**: [假设信息]

**评审人**: AI Assistant

**评审时间**: [当前时间]

### 1. 关键问题（必须修复）

- 

  [严重] 问题描述 + 代码位置 + 修复建议

- 

  [高危] 问题描述 + 代码位置 + 修复建议

### 2. 改进建议（建议修复）

- 

  [建议] 问题描述 + 代码位置 + 优化方案

### 3. 代码亮点（做得好）

- 

  [优秀] 亮点描述 + 代码位置

### 4. 具体修改示例

```
// 修改前
[有问题的代码]

// 修改后
[修复后的代码]
```

### 5. SonarQube规则映射

| 问题类型 | Sonar规则ID | 严重程度 | 是否通过 |
| -------- | ----------- | -------- | -------- |
| [问题]   | [规则ID]    | [级别]   | ❌        |

### 6. 总体评分

- 

  代码规范：8/10

- 

  代码质量：7/10

- 

  安全性：9/10

- 

  性能：6/10

- 

  可维护性：8/10

  **综合评分：76/100**

```
# EXAMPLE
## 示例代码
```

java

public class UserService {

public User getUser(String id) {

String sql = "SELECT * FROM users WHERE id = " + id;

// 执行查询...

}

}

```
## 示例评审输出
```

### 1. 关键问题

- 

  [严重] SQL注入漏洞：第3行使用字符串拼接，应使用预编译语句

  修复：使用PreparedStatement或MyBatis的#{}占位符

### 2. SonarQube映射

| 问题类型 | Sonar规则ID | 严重程度 |
| -------- | ----------- | -------- |
| SQL注入  | S3649       | Critical |

```

```

------

## 快速检查清单Prompt

```
请检查以下Java代码片段，返回一个检查清单：

1. **命名规范**（是否符合驼峰命名）
2. **代码注释**（是否有必要的Javadoc）
3. **异常处理**（是否捕获了应有异常）
4. **资源管理**（是否关闭了资源）
5. **并发安全**（是否有线程安全问题）
6. **性能问题**（是否有性能隐患）
7. **安全漏洞**（是否有安全隐患）

格式：
- [ ] 检查项1：[状态] 说明
- [ ] 检查项2：[状态] 说明

发现的问题请用"❌"标记，正确项用"✔️"标记。
```

------

## 使用建议

1. 

   **新项目开发**：使用完整模板，确保代码质量

2. 

   **现有代码优化**：使用Code Review专用模板

3. 

   **快速检查**：使用检查清单模板

4. 

   **团队规范**：可定制化添加公司内部规范

记住：**越详细的约束，得到的结果越符合预期**。告诉AI具体的规范编号（如"阿里巴巴Java开发手册1.7.0第6条"）会让结果更精准。