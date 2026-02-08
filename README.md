# Jbang QuickStart

用于 jbang 工具使用

[jbang - Unleash the power of Java by Max Rydahl Andersen](https://www.youtube.com/watch?v=cpKwBbz1sf0)

## 安装 jbang

## 安装 SDKMan 到 `/tmp/sdkman`

备份并移除当前 `~/.sdkman`，然后安装到临时目录 `/tmp/sdkman`：

```bash
tar zcvf ~/sdkman-backup_$(date +%F-%kh%M).tar.gz -C ~/ .sdkman
rm -rf ~/.sdkman
export SDKMAN_DIR="/tmp/sdkman" && curl -s "https://get.sdkman.io" | bash
```

> 说明：使用 `SDKMAN_DIR` 环境变量可以临时指定安装目录为 `/tmp/sdkman`。

## 安装 JDK 25 (GraalVM) 和 `jbang`

使用 SDKMan 安装 Java 与 `jbang`：

```shell
sdk install java 25.0.2-graalce
sdk install jbang 0.136.0
```

安装后可验证：

```bash
jbang --version
```

更方便的做法是把所需的 SDK 列入项目根目录下的 `.sdkmanrc`，然后运行批量安装：

```bash
# 在仓库根目录（包含 .sdkmanrc）运行：
sdk env install
```

本仓库已提供一个示例 `.sdkmanrc`（包含 Java 25 和 jbang），以及 `init.sh` 初始化脚本会在本地创建并尝试执行 `sdk env install`（失败时回退到逐项安装）。

注：关闭当前shell窗口，再重新开启新的，才生效。

## 设置 Maven 本地仓库为 /tmp/repo

```bash
mkdir ~/.m2 
mkdir /tmp/repo
cat>~/.m2/settings.xml<<'EOF'
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
	<localRepository>/tmp/repo</localRepository>
</settings>

EOF
```


或者运行仓库内提供的初始化脚本：

```bash
./init.sh
```
（运行前可执行 `chmod +x init.sh`）
---

## 执行jbang 命令

```bash
jbang hello@jbangdev
```