# 并发安全示例

## 线程安全的单例

### ✅ 正确示例
```java
@Component
public class IdGenerator {
    private static final AtomicLong SEQUENCE = new AtomicLong(0);
    
    public long nextId() {
        return SEQUENCE.incrementAndGet();
    }
}
```

### ❌ 错误示例
```java
public class IdGenerator {
    private long sequence = 0;  // 非原子操作
    
    public long nextId() {
        return ++sequence;  // 多线程下可能重复
    }
}
```

## 关键点
- 使用AtomicLong等原子类保证线程安全
- 避免使用普通的long/int进行自增操作
- 多线程环境下注意共享变量的可见性和原子性
