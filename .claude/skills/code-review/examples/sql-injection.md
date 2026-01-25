# SQL注入漏洞示例

## 示例代码
```java
public class UserService {
    public User getUser(String id) {
        String sql = "SELECT * FROM users WHERE id = " + id;
        // 执行查询...
    }
}
```

## 评审输出

### 1. 关键问题
- [严重] SQL注入漏洞：第3行使用字符串拼接，应使用预编译语句
  修复：使用PreparedStatement或MyBatis的#{}占位符

### 2. SonarQube映射
| 问题类型 | Sonar规则ID | 严重程度 |
|---------|-----------|---------|
| SQL注入  | S3649     | Critical |

### 3. 修复示例
```java
// 使用MyBatis
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUser(@Param("id") String id);
}

// 或使用PreparedStatement
public User getUser(String id) {
    String sql = "SELECT * FROM users WHERE id = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, id);
        // 执行查询...
    }
}
```
