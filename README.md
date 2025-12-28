# Reflection demo of Graalvm native application

> From [GraalVM Native Image Reflect Config Demystified][1]

## 安装graavel agent 

```bash
sdk install java 25.0.1-graalce
sdk default java 25.0.1-graalce
```

## 生成jar包

```bash
./gradlew build
```

- 生成 `reflect-config.json`

```bash
java -agentlib:native-image-agent=config-merge-dir=./config -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```
将生成的 `reflect-config.json`文件放入 `src/main/resources/META-INF/native-image`目录下

## bootRun 运行JVM
 
```bash
./gradlew bootRun
```

- 输出

```txt
2025-12-28T04:53:12.499Z  INFO 42178 --- [native-reflection-json-demo] [  restartedMain] .e.d.NativeReflectionJsonDemoApplication : Started NativeReflectionJsonDemoApplication in 0.72 seconds (process running for 1.058)
```

## nativeRun 用native方式运行

```bash
 ./gradlew nativeRun 
```

- 输出
 
```txt
2025-12-28T04:51:24.678Z  INFO 41272 --- [native-reflection-json-demo] [           main] .e.d.NativeReflectionJsonDemoApplication : Started NativeReflectionJsonDemoApplication in 0.02 seconds (process running for 0.026)
```


## Comparation




## Reference

[1]: https://stevenpg.com/posts/graalvm-reflect-config-demystified/




