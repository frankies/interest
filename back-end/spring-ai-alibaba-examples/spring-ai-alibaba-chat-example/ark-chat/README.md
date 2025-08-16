# Spring AI Alibaba Ark Chat Example
# Spring AI 阿里巴巴 Ark 聊天示例

This is an example project demonstrating how to interact with ByteDance Volcano Engine's Ark LLM using Spring AI Alibaba. Since Ark LLM is fully compatible with the OpenAI API, the code implementation can directly reference the openai-chat example project.

这是一个示例项目，演示如何使用 Spring AI Alibaba 与字节跳动火山引擎的 Ark LLM 进行交互。由于 Ark LLM 完全兼容 OpenAI API，因此代码实现可以直接参考 openai-chat 示例项目。

## Module Description | 模块说明

- ark-chat-client: Demonstrates how to interact with Ark LLM using ChatClient
- ark-chat-client: 演示如何使用 ChatClient 与 Ark LLM 交互

- ark-chat-model: Demonstrates how to interact with Ark LLM using ChatModel
- ark-chat-model: 演示如何使用 ChatModel 与 Ark LLM 交互

## Requirements | 环境要求

- Java 17+
- Maven 3.6+
- Spring Boot 3.2.0+
- Spring AI 0.8.0+

## Configuration | 配置说明

Before running the example, you need to configure the following parameters in application.yml:
运行示例之前，需要在 application.yml 中配置以下参数：

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}  # Ark LLM API Key | Ark LLM 的 API Key
      model-id: ${OPENAI_MODEL_ID}  # Ark LLM Model ID (Access Point ID) | Ark LLM 的模型 ID（Access Point ID）
```

