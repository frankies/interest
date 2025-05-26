# 飞鸟云订阅地址解析

本项目用于批量检查 `.xyz` 域名链接的可用性，并支持从网页自动提取 `.xyz` 链接、解析域名 IP，适合批量检测和网络运维场景。

## 目录结构

- [`check_urls.py`](check_urls.py)：从 [`url.txt`](url.txt) 读取链接，并并发检查其可用性。
- [`scrap_check.py`](scrap_check.py)：从指定网页（如 GitHub 用户主页）提取所有以 `.xyz` 结尾的链接，并检查其可用性。
- [`host_parse.py`](host_parse.py)：将 [`url.txt`](url.txt) 中的域名解析为 IP，输出 `/etc/hosts` 格式映射。
- [`url.txt`](url.txt)：待检测的链接列表，每行一个 URL。
- [`pyproject.toml`](pyproject.toml)：项目依赖与元数据。
- [`.python-version`](.python-version)：指定 Python 版本（3.11）。

## 安装依赖

推荐使用 [pyproject.toml](pyproject.toml) 管理依赖：

```sh
uv sync
```

或直接安装依赖：

```sh
uv add beautifulsoup4 requests
```

## 使用方法

### 1. 检查 URL 可用性

```sh
uv run python check_urls.py
```
- 并发检查 [`url.txt`](url.txt) 中所有链接的可访问性。

### 2. 从网页提取 `.xyz` 链接并检测

```sh
uv run python scrap_check.py
```
- 默认从 `https://github.com/feiniaoyun` 提取所有 `.xyz` 链接并检测其可用性。

### 3. 解析域名为 IP（生成 hosts 映射）

```sh
uv run python host_parse.py
```
- 输出 [`url.txt`](url.txt) 中所有域名的 IP 映射，格式适用于 `/etc/hosts`。

## 依赖环境

- Python 3.11+
- requests
- beautifulsoup4

## 说明

- 所有脚本均为命令行工具，直接运行即可。
- 可根据需要修改 [`url.txt`](url.txt) 或脚本中的目标网页地址。