# 快速检查

快速检查代码片段是否有明显问题，返回检查清单。

## 使用方式
在聊天中输入：`/quick-check [代码片段或文件路径]`

## 检查项

### 1. 命名规范
是否符合驼峰命名规范

### 2. 代码注释
是否有必要的Javadoc注释

### 3. 异常处理
是否捕获了应有异常

### 4. 资源管理
是否正确关闭了资源（try-with-resources）

### 5. 并发安全
是否有线程安全问题

### 6. 性能问题
是否有明显的性能隐患

### 7. 安全漏洞
是否有安全隐患（SQL注入、XSS等）

## 输出格式
```
- [✔️] 命名规范：符合驼峰命名
- [❌] 资源管理：未使用try-with-resources
- [✔️] 异常处理：异常捕获完整
...
```

## 示例
```
/quick-check 
public class UserService {
    public User getUser(String id) {
        String sql = "SELECT * FROM users WHERE id = " + id;
        // 执行查询...
    }
}
```

快速检查这段代码的问题。
