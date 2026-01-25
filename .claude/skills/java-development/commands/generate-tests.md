# 生成单元测试

为Java类生成完整的单元测试代码。

## 使用方式
在聊天中输入：`/generate-tests [文件路径]`

## 功能说明
自动生成：
- 测试类框架（@SpringBootTest）
- 测试方法（@Test）
- Mock对象（@Mock、@InjectMocks）
- 测试数据准备
- 断言验证
- 异常测试

## 测试覆盖
- 正常流程测试
- 边界条件测试
- 异常情况测试
- 并发场景测试（如需要）

## 测试框架
- JUnit 5
- Mockito
- AssertJ
- Spring Boot Test

## 最佳实践
- 测试方法命名清晰（should_xxx_when_xxx）
- 使用Given-When-Then模式
- 测试独立性（不依赖执行顺序）
- 覆盖率 > 80%

## 示例
```
/generate-tests src/main/java/com/example/service/OrderService.java
```

为订单服务生成完整的单元测试。
