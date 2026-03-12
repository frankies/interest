# Nanobot - 简单聊天

一个基于 Python 的聊天机器人应用，支持多渠道集成。

## 前置条件

- Docker 和 Docker Compose
- Python 3.8+（本地开发环境）

## 快速开始


### 1. 构建 Docker 镜像

```bash
docker compose build
```

### 2. 创建本地状态目录

```bash
mkdir -p .nanobot
```

### 3. 初始化配置（首次启动）

首次运行时初始化配置：

```bash
docker compose --profile tools run --rm onboard
```

这将在 `.nanobot/` 目录中生成配置骨架。

## 使用方式

### 运行代理

运行单次代理执行：

```bash
docker compose --profile tools run --rm agent
```

或使用自定义消息：

```bash
docker compose run --rm nanobot agent -m "Hello"
```

### 启动网关

启动网关服务（例如 Telegram）：

```bash
docker compose --profile runtime up gateway
```

### WhatsApp 设置（Linux 主机网络模式）

终端 1 - 登录渠道：

```bash
docker compose --profile whatsapp run --rm channels-login
```

终端 2 - 启动网关：

```bash
docker compose --profile whatsapp run --rm gateway-host
```

## 目录结构

```
.
├── .nanobot/                  # 本地状态目录（首次运行时创建）
│   ├── config.json           # 主配置文件
│   ├── workspace/             # 工作目录
│   └── memory/                # 状态存储
├── .github/                   # GitHub 工作流和配置
├── docker/                    # Docker 构建文件
│   └── Dockerfile            # 容器镜像构建配置
├── docker-compose.yml        # 服务定义和 profiles
├── README.md                 # 本文件
└── SECURITY.md               # 安全实践和指南
```

## 配置

配置文件在首次启动期间自动在 `.nanobot/` 中生成。关键文件包括：
- `config.json` - 主应用配置
- `workspace/` - 运行时工作目录
- `memory/` - 持久化状态和内存存储

### AI 模型接入配置

Nanobot 支持多种 AI 模型的接入。在 `config.json` 中配置以下内容：

```json
{
  "models": {
    "default": "openai",
    "openai": {
      "api_key": "sk-...",
      "model": "gpt-4",
      "temperature": 0.7
    },
    "alternative": {
      "type": "local",
      "endpoint": "http://localhost:8000",
      "model": "llama2"
    }
  }
}
```

**支持的模型类型：**
- **OpenAI** - ChatGPT 和 GPT 模型系列
  - 需要 API Key（环境变量 `OPENAI_API_KEY`）
  - 配置参数：`model`、`temperature`、`max_tokens` 等
  
- **本地模型** - 本地部署的 LLM
  - 需要本地服务端点
  - 支持 Ollama、LLaMA 等
  
- **其他服务** - 自定义 API 端点
  - 配置 `endpoint` 和 `auth` 信息

**配置方式：**
1. 手动编辑 `.nanobot/config.json`
2. 运行 `docker compose --profile tools run --rm onboard` 进行交互式配置
3. 通过环境变量传入：`-e OPENAI_API_KEY=sk-...`

### Gateway 的接入配置

Gateway 用于连接各种通讯渠道（Telegram、WhatsApp、Discord 等）。

**配置步骤：**

#### Telegram Gateway
```bash
# 1. 获取 Bot Token
#    - 在 Telegram 中与 @BotFather 对话
#    - 创建新 bot 并获取 token

# 2. 配置 config.json
cat >> .nanobot/config.json <<EOF
{
  "gateway": {
    "telegram": {
      "enabled": true,
      "token": "YOUR_BOT_TOKEN"
    }
  }
}
EOF

# 3. 启动 Gateway
docker compose --profile runtime up gateway
```

#### WhatsApp Gateway
```bash
# 1. 登录认证
docker compose --profile whatsapp run --rm channels-login

# 2. 启动 Gateway（使用 host 网络）
docker compose --profile whatsapp run --rm gateway-host
```

**Gateway 支持的渠道：**
- **Telegram** - 需要 Bot Token
- **WhatsApp** - 需要登录认证
- **Discord** - 需要 Bot Token 和 Server ID
- **Slack** - 需要 App Token 和 Bot Token
- **WeChat** - 需要相应配置

**Gateway 配置项：**
```json
{
  "gateway": {
    "host": "0.0.0.0",
    "port": 18790,
    "channels": {
      "telegram": {
        "enabled": true,
        "token": "YOUR_TOKEN"
      },
      "whatsapp": {
        "enabled": false,
        "phone": "+1234567890"
      }
    }
  }
}
```

**常见问题：**
- 端口被占用：修改 `port` 配置或使用 `docker-compose.yml` 中的端口映射
- 认证失败：确保 token 正确且未过期
- 消息未送达：检查 gateway 日志 `docker compose logs gateway`

## 项目结构详情

### Docker 设置
`docker/Dockerfile` 基于官方 Nanobot Docker 仓库，包含：
- Python 3.12 运行时环境
- Node.js bridge 功能
- Nanobot 源代码和依赖
- 容器启动时自动化 bridge 设置

**Dockerfile 源地址**：https://raw.githubusercontent.com/ciri/nanobot-docker/refs/heads/main/Dockerfile

### Docker Compose 服务
- **nanobot** - 主要容器，默认执行 status 命令
- **onboard** - 一次性设置和配置初始化
- **gateway** - 用于渠道集成的网关服务（Telegram、WhatsApp 等）
- **agent** - 代理执行服务，用于处理消息
- **channels-login** - WhatsApp/渠道认证服务

## 本地开发

### 不使用 Docker 进行本地开发

如果你更倾向于不使用 Docker 进行本地开发：

```bash
# 安装 Python 依赖
pip install -e .

# 直接运行 nanobot 命令
nanobot status
nanobot onboard
nanobot agent -m "test message"
```

## 支持

如有任何问题或疑问：
- 查看 [安全指南](SECURITY.md)
- 查阅 Docker Compose 配置
- 参考 [官方 Nanobot 文档](https://github.com/HKUDS/nanobot)