# Java 命名规范示例

## 接口命名

### ✅ 正确示例
```java
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);
}
```

### ❌ 错误示例
```java
public interface orderDao {  // 接口名应为大驼峰
    List<Order> find(Long uid, Integer st);  // 参数名不清晰
}
```

## 类命名和常量

### ✅ 正确示例
```java
public class OrderService {
    private static final int MAX_RETRY_COUNT = 3;
    
    public Order createOrder(CreateOrderRequest request) {
        // 方法行数不超过80行
        // 嵌套不超过3层
    }
}
```

### ❌ 错误示例
```java
public class orderService {  // 类名应为大驼峰
    private int maxRetryCount = 3;  // 常量应使用大写+下划线
}
```

## 检查清单
- [✔️] 类名使用大驼峰：OrderService
- [✔️] 方法名使用小驼峰：createOrder
- [✔️] 常量全大写：MAX_TIMEOUT_MINUTES
- [✔️] 布尔类型以is/has开头：isPaid
