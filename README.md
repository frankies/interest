# Simple Maven Project

这是一个使用Maven构建的Java Web项目。

## 项目信息

- **JDK版本**: 1.8
- **Maven版本**: 3.3.9 (使用Maven Wrapper)
- **项目类型**: Web应用 (WAR)
- **主要依赖**: Apache Oltu OAuth2 Client

## 项目结构

```
simple-maven-pj/
├── .mvn/                   # Maven Wrapper配置
│   ├── .m2/                # Maven本地仓库目录（由 .mvn/settings.xml 指定）
│   ├── wrapper/
│   │   └── maven-wrapper.properties
│   └── settings.xml        # Maven设置（本地仓库配置）
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       ├── HelloServlet.java
│   │   │       └── JndiCheckServlet.java
│   │   ├── resources/          # 资源文件目录（可选配置文件等）
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml
│   │       └── index.html
│   └── test/
│       └── java/
│           └── com/example/
│               ├── HelloServletTest.java
│               ├── JndiResourcesTest.java
│               └── TomcatTestSupport.java
├── .vscode/                 # VS Code 配置（tasks/launch/settings等）
│   └── tomcat/              # 本地 Tomcat/JNDI 相关脚本与 Context XML
├── mvnw                    # Maven Wrapper脚本（Unix/Linux/Mac）
├── mvnw.cmd                # Maven Wrapper脚本（Windows）
├── pom.xml                 # Maven项目配置
└── README.md               # 项目说明文档
```

## 依赖说明

- **javax.servlet:servlet-api** (2.4, provided): Servlet API，用于Web开发（兼容老容器/老项目）
- **org.apache.oltu.oauth2.client** (1.0.1): OAuth2客户端库
- **junit** (4.12): 单元测试框架

## 使用说明

### 构建项目

在Windows上使用：

```bash
mvnw.cmd clean install
```

在Unix/Linux/Mac上使用：

```bash
./mvnw clean install
```

### 编译项目

```bash
mvnw.cmd compile
```

### 运行测试

```bash
mvnw.cmd test
```

### 打包项目

```bash
mvnw.cmd package
```

打包后的WAR文件将位于 `target/simple-maven-pj.war`

## Maven配置

项目配置了自定义的Maven本地仓库路径：`.mvn/.m2/repository`

这意味着所有下载的依赖都会存储在项目目录的 `.mvn/.m2` 文件夹中，而不是系统默认的 `~/.m2` 目录。

## 部署说明

生成的WAR文件可以部署到任何支持Servlet 2.4+的Web容器中，例如：

- Apache Tomcat 8.x 或更高版本
- Jetty 9.x 或更高版本
- WildFly/JBoss

## 开发环境要求

- JDK 1.8 或更高版本
- Maven 3.3.9 (通过Maven Wrapper自动下载)

## VS Code开发环境

项目已配置完整的VS Code开发环境支持：

### 推荐扩展

安装以下扩展以获得最佳开发体验：

- **Java扩展包** - Java开发核心工具
- **Maven for Java** - Maven项目管理
- **Tomcat for Java** - 本地Tomcat服务器支持
- **XML** - XML文件支持
- **GitLens** - Git增强工具
- **SonarLint** - 代码质量检查

打开项目后，VS Code会提示安装推荐的扩展。

### VS Code任务

项目配置了以下Maven任务（按 `Ctrl+Shift+P`，输入"Run Task"）：

- `maven: compile` - 编译项目
- `maven: test` - 运行测试
- `maven: package` - 打包WAR文件
- `maven: install` - 清理并安装（默认构建任务，快捷键 `Ctrl+Shift+B`）
- `maven: clean install (skip tests)` - 跳过测试的快速构建

另外包含 Tomcat 相关任务：

- `tomcat: run` / `tomcat: debug` - 使用 `tomcat7-maven-plugin` 启动（debug 端口 8000）
- `tomcat: shutdown (tomcat7:shutdown)` - 优雅停止 Tomcat（推荐优先用这个）
- `tomcat: stop` - 强制停止（杀掉占用 8080 的监听进程，兜底用）
- `tomcat: run (a1stream JNDI)` / `tomcat: debug (a1stream JNDI)` - 启动时额外加载 `.vscode/tomcat/Catalina/localhost/*.xml` 的 JNDI DataSource

#### 通用 contextPath（推荐）

历史上该项目固定使用 `/a1stream` 作为 contextPath。现在已将其做成可配置，方便复用到其它项目：

- Maven 属性：`app.contextPath`（默认 `/a1stream`，见 `pom.xml`）
- VS Code：工作区设置 `applicationContextPath`（默认 `/a1stream`，见 `.vscode/settings.json`）

你可以用以下任一方式覆盖 contextPath：

1. 直接用 Maven 命令覆盖：

```bash
mvnw.cmd "-Dapp.contextPath=/demo" tomcat7:run
```

2. 用 VS Code 任务覆盖：

- 打开 `.vscode/settings.json`，修改 `applicationContextPath`（例如 `/demo`）

#### Context XML 命名规则（JNDI DataSource）

当你使用 `tomcat: run (a1stream JNDI)` / `tomcat: debug (a1stream JNDI)` 时，会根据 contextPath 自动选择 Context XML：

- `applicationContextPath=/a1stream` → 使用 `.vscode/tomcat/Catalina/localhost/a1stream.xml`
- `applicationContextPath=/demo` → 使用 `.vscode/tomcat/Catalina/localhost/demo.xml`
- `applicationContextPath=/` 或空 → 使用 `.vscode/tomcat/Catalina/localhost/ROOT.xml`

也就是说，后续迁移到其它项目时，通常只需要：

- 修改 `applicationContextPath`
- 复制一份 `.vscode/tomcat/Catalina/localhost/a1stream.xml` 为对应名称的 xml（并按项目修改 JDBC URL/账号等）

### 调试配置

- **Debug (Attach) - Tomcat** - 附加到运行中的Tomcat（端口8000）
- **Debug HelloServlet** - 直接调试Servlet类
- **Run All Tests** - 运行所有测试

### 代码片段

项目包含以下代码片段：

- `servlet` - 快速创建Servlet类
- `test` - 快速创建JUnit测试类
- `oauth2` - 快速创建OAuth2请求

## 注意事项

1. 首次运行Maven Wrapper时，它会自动下载Maven 3.3.9
2. 本地仓库配置在 `.mvn/settings.xml` 中
3. 项目使用UTF-8编码
4. 确保安装了JDK 1.8并正确配置了JAVA_HOME环境变量

## 本地 JNDI 可用性验证（可选）

项目提供了一个检测端点与对应的集成测试，用于验证 `java:comp/env/jdbc/*` 是否已绑定：

- 运行时 Servlet：`/__jndi`（见 `WEB-INF/web.xml` 映射）
- 集成测试：`JndiResourcesTest`

如果你切换了 contextPath（例如 `/demo`），并且也准备了对应的 Context XML（例如 `demo.xml`），可这样跑单测：

```bash
mvnw.cmd "-Dapp.contextPath=/demo" "-Dtest=JndiResourcesTest" test
```
