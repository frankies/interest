# product-analysis-graph

## Spring AI Alibaba Graph 并行节点 (ParallelNode) 演示：智能商品信息丰富化

本项目是一个基于 Spring AI Alibaba Graph 实现的“智能商品信息丰富化”示例模块。它主要演示了如何利用并行图（Parallel Graph）的能力，同时处理商品描述文本，以生成营销文案和提取结构化商品属性，最终合并为一个完整的商品信息对象。

## 场景描述

在一个电商后台系统中，运营人员只需提供一段商品的原始描述文本，例如：

"一款高品质、舒适的纯棉T恤，有蓝、红、绿三种颜色可选，适合夏季穿着。"

系统后台的 AI 图（Graph）接收到这段文本后，将并行执行以下两个独立的任务：

1.  **营销文案生成 (Marketing Copy Generation)**：一个 AI 节点负责根据商品描述，生成一句吸引人的、简短的营销口号（Slogan）。
2.  **规格参数提取 (Specification Extraction)**：另一个 AI 节点负责从描述中提取结构化的商品属性，例如 `{ "材质": "纯棉", "颜色": ["蓝", "红", "绿"], "季节": "夏季" }`。

最后，一个合并节点（Merge Node）会将营销口号和规格参数汇总成一个完整的、丰富的商品信息对象（JSON 格式），然后返回给前端或存入数据库。

## 优势

*   **实用性强**：这是电商、内容管理等领域的常见需求。
*   **清晰地体现并行优势**：生成营销文案和提取参数是两个完全独立的语言模型任务，可以同时进行，能有效缩短处理总时长。
*   **逻辑清晰**：输入 -> 并行处理 -> 合并输出，这个流程非常符合 Spring AI Alibaba Graph 的并行图结构。

## 技术实现

### 核心组件

*   **`ProductGraphConfiguration.java`**：定义了商品分析图的结构。它包含三个主要节点：
    *   `marketingCopyNode`：负责生成营销口号。
    *   `specificationExtractionNode`：负责提取商品规格。
    *   `mergeNode`：负责合并两个并行任务的结果。
    图的边定义了任务的并行执行流程：从 `START` 同时触发营销文案生成和规格提取，然后两个任务的结果都汇聚到 `merge` 节点，最后到达 `END`。
*   **`Product.java`**：定义了最终输出的商品信息数据模型，包含 `slogan`、`material`、`colors` 和 `season` 字段。
*   **`ProductController.java`**：提供了一个 RESTful API (`/product/enrich`)，接收商品描述作为输入，并调用 `productAnalysisGraph` 来处理请求，返回丰富后的商品信息。
*   **`application.yml`**：Spring Boot 配置文件，用于配置应用名称、端口以及 DashScope AI 服务的 API 密钥和模型。

### 依赖

本项目依赖于 Spring Boot Web Starter、Spring AI Alibaba DashScope Starter 和 Spring AI Alibaba Graph Core，以提供 Web 服务能力、AI 模型集成和图编排能力。

## 如何运行

### 前提条件

*   Maven
*   Java 17 或更高版本
*   有效的阿里云灵积（DashScope）API Key

### 步骤

1.  **设置 API 密钥**：
    在项目的根目录（`spring-ai-alibaba-examples/`）下的 `key.env` 文件中设置您的 DashScope API Key：
    ```properties
    AI_DASHSCOPE_API_KEY=YOUR_DASHSCOPE_API_KEY
    ```
    请将 `YOUR_DASHSCOPE_API_KEY` 替换为您的实际 API 密钥。

2.  **构建项目**：
    在 `spring-ai-alibaba-examples/` 项目的根目录运行 Maven 命令来构建整个项目：
    ```bash
    mvn clean install
    ```

3.  **运行应用**：
    导航到 `product-analysis-graph` 模块目录，并运行 Spring Boot 应用：
    ```bash
    cd spring-ai-alibaba-graph-example/product-analysis-graph
    mvn spring-boot:run
    ```
    应用将在 `http://localhost:8080` 启动。

4.  **测试端点**：
    应用启动后，您可以使用 `curl` 命令或任何 API 测试工具（如 Postman, IntelliJ IDEA 的 HTTP Client）向 `/product/enrich` 端点发送 POST 请求。

    **使用 `curl` 示例：**
    ```bash
    curl -X POST http://localhost:8080/product/enrich \
    -H "Content-Type: text/plain" \
    -d "一款高品质、舒适的纯棉T恤，有蓝、红、绿三种颜色可选，适合夏季穿着。"
    ```

    **使用 `product-enrich.http` 文件：**
    在本项目模块根目录下提供了 `product-enrich.http` 文件，您可以在支持 `.http` 文件的 IDE 中直接运行。

### 预期输出

您将收到一个 JSON 响应，其中包含由 AI 生成的营销口号和提取的规格，例如：

```json
{
  "slogan": "清凉一夏，纯棉随心 —— 舒适本真，尽在一抹色彩！",
  "material": "纯棉",
  "colors": [
    "蓝",
    "红",
    "绿"
  ],
  "season": "夏季"
}
```
