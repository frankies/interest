# Spring 实现 3 种异步流式接口

## 介绍

利用spring 3 个接口类：

- **ResponseBodyEmitter**

```shell
curl http://localhost:8080/async/bodyEmitter
```

- **SseEmitter**

- **StreamingResponseBody** 

```shell
http://localhost:8080/async/streamingResponse
```

实现异步从后台推数据到前台

详见： `AsynTestController`类

## 启动

需要jdk8或以上JVM环境

启动命令：

```shell
mvnw  spring-boot:run
```

## 访问入口

[http://localhost:8080/](http://localhost:8080/)