<https://www.jbang.dev/>

<https://www.youtube.com/watch?v=cpKwBbz1sf0>

    $ sdk install jbang

    $ jbang -c 'println("hello");'
    WARNING: Using incubator modules: jdk.incubator.vector
    hello

    $ jbang -c 'println("hello, " + args[0]);' world
    WARNING: Using incubator modules: jdk.incubator.vector
    hello, world

#### &#x20;限定Java 版本

    $ jbang --java 25 -i 
    WARNING: Using incubator modules: jdk.incubator.vector
    |  Welcome to JShell -- Version 25.0.2
    |  For an introduction type: /help intro

    jshell> 

*   如果指定JDK版本没有找不到，它会自动下载

#### 使用刚才Java文件

    $ jbang --java 25 script.java 
    [jbang] Building jar for script.java...
    Hellow world!

#### 将 jbang 别名成 `j!`

```shell
$ alias "j!=jbang"

$ j! jdk l
Installed JDKs (<=default):
   25 (25.0.2+10-LTS)
```

#### 在 java文件里指定 Java版本

```
$ cat script_jbang.java 
//JAVA 25

void main() {

   IO.println("Hellow world!");
}
$ j! script_jbang.java 
Hellow world!


```

还可以这样初始化java文件

```bash
$ jbang init script_jbang.java 
$ cat script_jbang.java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+

void main(String... args) {
    IO.println("Hello World");
}


```

#### 还可以这样定义依赖，启动时自动下载依赖

```shell
$ cat App.java 
///usr/bin/env jbang "$0" "$@" ; exit $?
// Give a sample code of jbang script
//DEPS ch.qos.logback:logback-classic:1.5.18

void main() {
    var log = org.slf4j.LoggerFactory.getLogger("MyApp");
    log.info("Hello, JBang with Logback!");
}

$ j! App.java 
15:59:02.732 [main] INFO MyApp -- Hello, JBang with Logback!


```

#### &#x20;代码调试

```bash
$ j! --debug  App.java
Listening for transport dt_socket at address: 4004
```

#### &#x20;一个命令初始化Quarkus 项目的http代码

```bash
$ jbang init -t qrest myservice.java
[jbang] File initialized. You can now run it with 'jbang myservice.java' or edit it using 'jbang edit --open=[editor] myservice.java' where [editor] is your editor or IDE, e.g. 'cursor'. If your IDE supports JBang, you can edit the directory instead: 'jbang edit . myservice.java'. See https://jbang.dev/ide

$ cat myservice.java

///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 17+
// Update the Quarkus version to what you want here or run jbang with
// `-Dquarkus.version=<version>` to override it.
//DEPS io.quarkus:quarkus-bom:${quarkus.version:3.15.1}@pom
//DEPS io.quarkus:quarkus-rest
// //DEPS io.quarkus:quarkus-smallrye-openapi
// //DEPS io.quarkus:quarkus-swagger-ui
//JAVAC_OPTIONS -parameters

import io.quarkus.runtime.Quarkus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/hello")
@ApplicationScoped
public class myservice {

    @GET
    public String sayHello() {
        return "Hello from Quarkus with jbang.dev";
    }

}

$ ./myservice.java 
[jbang] Dependencies resolved
[jbang] Building jar for myservice.java...
[jbang] Post build with io.quarkus.launcher.JBangIntegration
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.jboss.threads.JBossExecutors (file:/home/codespace/.m2/repository/org/jboss/threads/jboss-threads/3.6.1.Final/jboss-threads-3.6.1.Final.jar)
WARNING: Please consider reporting this to the maintainers of class org.jboss.threads.JBossExecutors
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
[jbang] JBoss Threads version 3.6.1.Final
[jbang] Quarkus augmentation completed in 1557ms
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.jboss.threads.JBossExecutors (file:/home/codespace/.m2/repository/org/jboss/threads/jboss-threads/3.6.1.Final/jboss-threads-3.6.1.Final.jar)
WARNING: Please consider reporting this to the maintainers of class org.jboss.threads.JBossExecutors
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2026-02-07 16:06:49,802 INFO  [io.quarkus] (main) quarkus 999-SNAPSHOT on JVM (powered by Quarkus 3.15.1) started in 0.760s. Listening on: http://0.0.0.0:8080
2026-02-07 16:06:49,808 INFO  [io.quarkus] (main) Profile prod activated. 
2026-02-07 16:06:49,808 INFO  [io.quarkus] (main) Installed features: [cdi, rest, smallrye-context-propagation, vertx]



$ curl localhost:8080/hello
Hello from Quarkus with jbang.dev
```

#### &#x20;导出Jar包

```bash
$ jbang  export portable --fresh --force  myservice.java
[jbang] Resolving dependencies...
[jbang]    io.quarkus:quarkus-bom:3.15.1@pom
[jbang]    io.quarkus:quarkus-rest:3.15.1
[jbang] Dependencies resolved
[jbang] Building jar for myservice.java...
[jbang] Post build with io.quarkus.launcher.JBangIntegration
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.jboss.threads.JBossExecutors (file:/home/codespace/.m2/repository/org/jboss/threads/jboss-threads/3.6.1.Final/jboss-threads-3.6.1.Final.jar)
WARNING: Please consider reporting this to the maintainers of class org.jboss.threads.JBossExecutors
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
[jbang] JBoss Threads version 3.6.1.Final
[jbang] Quarkus augmentation completed in 1432ms
[jbang] Updating jar...
[jbang] Exported to /workspaces/interest/java-25/myservice.jar


$ jbang myservice.jar 
WARNING: A terminally deprecated method in sun.misc.Unsafe has been called
WARNING: sun.misc.Unsafe::objectFieldOffset has been called by org.jboss.threads.JBossExecutors (file:/workspaces/interest/java-25/lib/jboss-threads-3.6.1.Final.jar)
WARNING: Please consider reporting this to the maintainers of class org.jboss.threads.JBossExecutors
WARNING: sun.misc.Unsafe::objectFieldOffset will be removed in a future release
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2026-02-07 16:30:58,322 INFO  [io.quarkus] (main) quarkus 999-SNAPSHOT on JVM (powered by Quarkus 3.15.1) started in 0.765s. Listening on: http://0.0.0.0:8080
2026-02-07 16:30:58,329 INFO  [io.quarkus] (main) Profile prod activated. 
2026-02-07 16:30:58,330 INFO  [io.quarkus] (main) Installed features: [cdi, rest, smallrye-context-propagation, vertx]
^C2026-02-07 16:31:00,746 INFO  [io.quarkus] (main
```

#### &#x20;更多参考代码

<https://github.com/jbangdev/jbang-examples/tree/69abd9e1bd39efc5fe5884f77351d3916412d509/examples>

<https://github.com/aws-samples/serverless-patterns/tree/main/lambda-jbang>

#### &#x20;Jbang Catalog

定义了很多应用的别名和模板的别名

<https://www.jbang.dev/documentation/jbang/latest/alias_catalogs.html>

<https://github.com/jbangdev/jbang-catalog/blob/main/jbang-catalog.json>

#### &#x20;使用现有的别名

```bash
$ j! hello@jbangdev frankies
Hello frankies

```

*   例子是**打印本地的环境变量**

    &#x20; 实际上是 <https://github.com/jbangdev/jbang-catalog/blob/main/jbang-catalog.json> 里定义的 `env`别名

    &#x20; 这个地址是gitlab/github上  `/<usr/org>/jbang-catalog/jbang-catalog.json` 配

> <https://www.jbang.dev/documentation/jbang/latest/alias_catalogs.html>&#x20;
>
> Examples:
>
> `jbang `[`tree@jbang.dev`](mailto\:tree@jbang.dev) will (because of the dot in the name) will lookup a catalog first at <https://jbang.dev/jbang-catalog.json>.
>
> `jbang hello@jbangdev` will run the alias `hello` as defined in `jbang-catalog.json` found in <https://github.com/jbangdev/jbang-catalog>.
>
> This allows anyone to provide a set of jbang scripts defined in their website or in a github, gitlab or bitbucket repositories.
>
> The full format is `<alias>@<user/org>(/repository)(/branch)(~path)` or `<alias>@<hostname>(/path/to/catalog)` allowing you to do things like:

已定义可用的别名列表：

### ️ JBang 脚本工具列表

&#x20;  使用命令格式：`jband <名称>@jbangdev` <参数...可选>

| 名称             | 脚本引用                                                                                                               | 描述                                  | 示例                                                             |
| -------------- | ------------------------------------------------------------------------------------------------------------------ | ----------------------------------- | -------------------------------------------------------------- |
| hello          | hello.java                                                                                                         | 针对每个参数进行回显问候的脚本                     | jbang hello`@jbangdev` World                                   |
| properties     | properties.java                                                                                                    | 转储系统属性表                             | jbang properties`@jbangdev`                                    |
| deps           | deps.java                                                                                                          | 分析 JBang 脚本的依赖关系                    | jbang deps`@jbangdev` myscript.java                            |
| env            | env.java                                                                                                           | 转储环境变量表                             | jbang env`@jbangdev`a                                          |
| gavsearch      | gavsearch.java                                                                                                     | 从命令行使用 search.maven.org 进行搜索。       | jbang gavsearch`@jbangdev` hibernate                           |
| git            | jgit.java                                                                                                          | 基于 jgit 实现的 Git 命令行工具。              | jbang jgit`@jbangdev` clone <https://github.com/user/repo.git> |
| bouncinglogo   | bouncinglogo.java                                                                                                  | -                                   | jbang bouncinglogo\@jbangdev                                   |
| h2             | com.h2database\:h2:1.4.200                                                                                         | -                                   | jbang h2\@jbangdev                                             |
| catalog2readme | catalog2readme.java                                                                                                | -                                   | jbang catalog2readme.java                                      |
| httpd          | httpd.java                                                                                                         | 运行一个 Web 服务器，用于分发目录内容。              | jbang httpd\@jbangdev -d \_site                                |
| getjava        | getjava.java                                                                                                       | 使用 api.foojay.io 下载 Java 发行版的实验性工具。 | jbang getjava.java 17                                          |
| ec             | ec.jsh                                                                                                             | -                                   | jbang ec\@jbangdev                                             |
| faker          | faker.jsh                                                                                                          | -                                   | jbang faker\@jbangdev                                          |
| dalle          | dalle.java                                                                                                         | -                                   | jbang dalle.java "A beautiful sunset"                          |
| bootstrap      | bootstrap.java                                                                                                     | 引导 JBang 脚本，使其成为自包含的脚本。             | jbang bootstrap\@jbangdev myscript.java                        |
| jmc            | jmc.jsh                                                                                                            | -                                   | jbang jmc.jsh                                                  |
| mf             | mf/mf.java                                                                                                         | JAR 清单读取器命令行工具。                     | jbang mf\@jbangdev mylib.jar                                   |
| jbang-fmt      | jbang-fmt\@jbangdev/jbang-fmt                                                                                      | 格式化 Java 代码（不会破坏 JBang 指令）。         | jbang jbang-fmt\@jbangdev myscript.java                        |
| jbang-jupyter  | <https://github.com/jbangdev/jbang-jupyter/releases/download/early-access/jbang-jupyter-0.1.0-SNAPSHOT-fatjar.jar> | -                                   | jbang jbang-jupyter.java                                       |
| trylink        | jupyter/trylink.java                                                                                               | -                                   | jbang trylink.java                                             |

另外一个可用仓库：<https://github.com/jbangdev/jbang-examples/>

```shell
jbang -x hibernate@jbangdev/jbang-examples
jbang -x junitrun@jbangdev/jbang-examples
```

#### 本地定义别名

> <https://www.jbang.dev/documentation/jbang/latest/alias_catalogs.html#local-alias-catalogs>
>
> 1.  Current directory, `./jbang-catalog.json`
> 2.  In `./.jbang/jbang-catalog.json`
> 3.  In the parent directory, `../jbang-catalog.json`
> 4.  In the parent’s `.jbang` directory, `../.jbang/jbang-catalog.json`
> 5.  And repeating steps 3 and 4 recursively upwards to the root of the file system
> 6.  As the last step it will look in `$HOME/.jbang/jbang-catalog.json`

#### 添加别名并使用

```bash
$ jbang alias add --description="Best hello world application" --name best_hello https://github.com/jbangdev/jbang-examples/blob/HEAD/examples/helloworld.java
[jbang] Alias 'best_hello' added to '/home/codespace/.jbang/jbang-catalog.json'

$ jbang alias list --show-origin
/home/codespace/.jbang/jbang-catalog.json
   best_hello
      Best hello world application
      https://github.com/jbangdev/jbang-examples/blob/HEAD/examples/helloworld.java

$ j! best_hello
[jbang] Building jar for helloworld.java...
Hello World!
```

### 构建和安装本地命令，需要重启shell窗口

<https://www.jbang.dev/documentation/jbang/latest/native-images.html>

#### 将脚本转成本地命令：

需要先安装 gravvlvm

```bash
$ sdk install java 25.0.1-graal

Downloading: java 25.0.1-graal

In progress...

####################################################################################################################################### 100.0%


Repackaging Java 25.0.1-graal...

Done repackaging...

Installing: java 25.0.1-graal
Done installing!
```

开始本地构建

```bash
$ cat>FastApp.java<<'EOF'
///usr/bin/env jbang "$0" "$@" ; exit $?
//NATIVE_OPTIONS -O2 --no-fallback --enable-preview

class FastApp {
    public static void main(String[] args) {
        System.out.println("Native app running!");
    }
}
EOF

$ jbang --native FastApp.java 
[jbang] Building jar for FastApp.java...
[jbang] log: /tmp/jbang13700272908641685690native-image
Native app running!

$ jbang app install -n  FastApp.java # 再运行 FastApp
[jbang] Command installed: FastApp

$ jbang app install --name Fast FastApp.java # 改成别的名称
```

或者来自远程

<https://www.jbang.dev/documentation/jbang/latest/app-installation.html>

```bash

 ## 参考：https://github.com/quarkusio/jbang-catalog/blob/main/jbang-catalog.json
$ jbang app install  quarkus@quarkusio

 # Install from Maven coordinates
$jbang app install info.picocli:picocli-codegen:4.6.3

```



### Install with Specific Java Version

    # Install with specific Java version
    jbang app install --java 17 script.java

### Install Native Images

    # Install as native image (requires GraalVM)
    jbang app install --native mytool.java

## Installation Locations

**Windows**:

*   Apps installed to: `%USERPROFILE%\.jbang\bin`
*   Added to system PATH

**Unix/Linux/macOS**:

*   Apps installed to: `~/.jbang/bin`
*   Added to shell PATH

### 支持多种语言



<https://www.jbang.dev/documentation/jbang/latest/multiple-languages.html>



#### Kotlin

```kt
///usr/bin/env jbang "$0" "$@" ; exit $?
//KOTLIN 2.3.0
//JAVA 25
//DEPS org.jetbrains.kotlin:kotlin-stdlib:2.0.21
//DEPS com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class Person(val name: String, val age: Int)

fun main(args: Array<String>) {
    val mapper = jacksonObjectMapper()
    val person = Person("Alice", 30)
    val json = mapper.writeValueAsString(person)
    println("JSON: $json")

    val parsed = mapper.readValue<Person>(json)
    println("Parsed: $parsed")
}

```



## 相关的Template

<https://www.jbang.dev/documentation/jbang/latest/templates.html>

    jbang template list

&#x20;

## &#x20;查询可用的 Catalog

```bash
## 查看所有可用市场
jbang catalog list

## 查看某个市场所有
jbang catalog list jreleaser
```



