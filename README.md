# Markdown 样式工作区

这个工作区用于创建和测试精美的 Markdown 样式表，仅作用于**VSCode**下。

## 📁 项目结构

```
.
├── .vscode/
│   ├── markdown-kiro-dark.css    # Kiro Dark 深色主题样式
│   ├── markdown-kiro-light.css   # Kiro Light 浅色主题样式
│   └── settings.json              # VS Code 配置文件
├── test.md                        # Markdown 测试文件
└── README.md                      # 项目说明文档
```

## 🎨 样式主题

### Kiro Dark 风格
- 深色渐变背景（深蓝到深灰）
- 紫色和蓝色渐变主题色
- 发光和阴影效果
- 现代化的设计风格
- 适合夜间阅读

### Kiro Light 风格
- 明亮的白色渐变背景
- 相同的紫蓝色渐变主题
- 柔和的阴影效果
- 清晰的文字对比度
- 适合日间阅读

## 🚀 使用方法

1. 在 VS Code 中打开 `test.md` 文件
2. 在 `.vscode/settings.json` 中配置要使用的样式：
   ```json
   {
     "markdown.styles": [
       ".vscode/markdown-kiro-dark.css"
     ]
   }
   ```
3. 使用 Markdown 预览功能查看效果

## ✨ 支持的样式元素

- ✅ 标题（H1-H6）
- ✅ 段落和文本格式（粗体、斜体、删除线）
- ✅ 代码块和行内代码
- ✅ 链接
- ✅ 表格
- ✅ 引用块
- ✅ 列表（有序、无序）
- ✅ 图片
- ✅ 水平线
- ✅ 任务列表
- ✅ 自定义提示框（warning、info、note）

## 📝 提示框使用示例

使用 HTML 标签创建彩色提示框：

```html
<div class="warning">
⚠️ 警告内容
</div>

<div class="info">
ℹ️ 信息内容
</div>

<div class="note">
📝 笔记内容
</div>
```

## 🎯 特色功能

- 渐变色背景和边框
- 平滑的悬停动画效果
- 自定义滚动条样式
- 响应式设计，支持移动端
- 代码块顶部装饰条
- 引用块大引号装饰

## 📄 许可

自由使用和修改
