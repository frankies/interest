# 创建企业级服务

创建一个符合企业级标准的Spring Boot服务模块，包含完整的分层架构。

## 使用方式
在聊天中输入：`/create-service [服务名称]`

## 功能说明
自动生成包含以下内容的服务模块：
- Controller层（REST接口 + DTO）
- Service层（业务逻辑 + 接口实现）
- Domain层（实体类 + 枚举）
- Repository层（数据访问）
- 异常处理（全局异常处理器）
- 配置类（Redis、MyBatis-Plus、线程池）

## 代码规范
- 严格遵守《阿里巴巴Java开发手册》v1.7.0
- Google Java Style 代码风格
- 完整的Javadoc注释
- 构造器注入而非字段注入
- 使用Lombok简化代码

## 示例
```
/create-service Order
```

生成订单服务的完整代码结构。
