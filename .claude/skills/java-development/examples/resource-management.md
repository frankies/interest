# 资源管理示例

## try-with-resources

### ✅ 正确示例
```java
public String readTemplate(String filePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        return br.lines().collect(Collectors.joining("\n"));
    } catch (IOException e) {
        throw new BusinessException("读取模板失败", e);
    }
}
```

### ❌ 错误示例
```java
public String readTemplate(String filePath) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(filePath));
    return br.readLine();  // 异常时资源泄漏
}
```

## 最佳实践
- 使用try-with-resources自动关闭资源
- 捕获并转换为业务异常
- 避免资源泄漏
- 确保异常情况下资源也能正确释放
