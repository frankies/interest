# 修复问题

根据代码审查结果，自动修复发现的问题。

## 使用方式
在聊天中输入：`/fix-issues [文件路径] [问题类型]`

## 可修复的问题类型

### 1. 命名规范问题
- 类名不符合大驼峰
- 方法名不符合小驼峰
- 常量未使用大写+下划线
- 变量名不清晰

### 2. 代码风格问题
- 缺少注释
- 代码格式不规范
- 导入语句混乱

### 3. 性能问题
- 字段注入改为构造器注入
- 添加缓存注解
- 优化循环中的操作
- 集合初始化容量

### 4. 安全问题
- SQL拼接改为参数化查询
- 添加参数校验
- 敏感信息脱敏
- 添加权限校验

### 5. 资源管理问题
- 改用try-with-resources
- 添加finally块关闭资源

### 6. 并发安全问题
- 使用原子类替代普通变量
- 添加synchronized或Lock
- 使用线程安全的集合

## 示例
```
/fix-issues src/main/java/com/example/service/UserService.java security
```

修复用户服务中的安全问题。

```
/fix-issues src/main/java/com/example/service/OrderService.java performance
```

修复订单服务中的性能问题。

```
/fix-issues src/main/java/com/example/service/PaymentService.java all
```

修复支付服务中的所有问题。
