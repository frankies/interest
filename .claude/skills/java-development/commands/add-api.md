# 添加REST API接口

为现有服务添加符合规范的REST API接口。

## 使用方式
在聊天中输入：`/add-api [接口描述]`

## 功能说明
自动生成：
- Controller方法（包含完整注解）
- 请求/响应DTO
- Service接口和实现
- 参数校验（@Valid、@NotNull等）
- OpenAPI文档注解
- 异常处理

## 最佳实践
- 使用RESTful风格（GET、POST、PUT、DELETE）
- 统一响应格式（Result<T>）
- 参数校验和错误处理
- 接口文档完整（@Operation、@ApiResponse）
- 权限校验（@PreAuthorize）

## 示例
```
/add-api 创建订单接口，支持优惠券和库存校验
```

生成完整的创建订单API代码。
