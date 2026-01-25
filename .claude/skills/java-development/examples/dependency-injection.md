# 依赖注入和缓存示例

## 构造器注入（推荐）

### ✅ 正确示例
```java
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;  // 构造器注入
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Cacheable(value = "order", key = "#orderId")
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }
}
```

### ❌ 错误示例
```java
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired  // 字段注入，不推荐
    private OrderRepository orderRepository;
    
    public Order getOrderById(Long orderId) {
        // 缺少缓存，每次查询数据库
        return orderRepository.findOne(orderId);  // 方法名不规范
    }
}
```

## 最佳实践
- 使用构造器注入而非字段注入
- 添加缓存注解减少数据库压力
- 使用Optional处理可能为空的返回值
- 抛出业务异常而非返回null
