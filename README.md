# py-simple-chat

一个基于 Flask 和 Flask-SocketIO 的简单聊天室项目。

## 功能
- 用户注册、登录
- 公共聊天、私聊
- 好友添加与请求
- 管理员功能：禁言、踢人、封号
- 在线用户显示

## 依赖环境
- Python 3.7+
- Flask
- Flask-SocketIO
- uv（推荐用于依赖管理和安装）


## 环境准备

1. 安装 Python 3.7 及以上版本（可从 https://www.python.org/downloads/ 下载并安装）。
2. 安装 uv（推荐，需先安装 Python）：

    Windows 下可在命令行执行：
    ```bash
    pip install uv
    ```
    更多 uv 详情见：https://github.com/astral-sh/uv

## 快速开始

1. 安装依赖（推荐使用 uv）：

```bash
uv venv
uv pip install -r requirements.txt
```

如在中国大陆，建议使用国内 PyPI 镜像源加速依赖安装。例如：

- 清华大学镜像：
    ```bash
    uv pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple
    ```
- 阿里云镜像：
    ```bash
    uv pip install -r requirements.txt -i https://mirrors.aliyun.com/pypi/simple/
    ```

也可直接用 pip：
```bash
pip install -r requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple
```

2. 启动服务：

```bash
uv run python app.py
```

3. 访问页面：

浏览器打开 http://localhost:5000

## 目录结构

```
app.py
requirements.txt
.gitignore
.gitattributes
templates/
    index.html
```

## 说明
- 用户、好友、封禁等数据以 json 文件存储在项目根目录。
- 管理员用户名为 `admin`，可在 app.py 中修改。

---

如需自定义或扩展功能，请参考 app.py 代码。
