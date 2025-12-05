# SpringBoot 4.0 预览版本练习


## 借助docker镜像生成

```shell
./gradlew bootBuildImage
```

就会自动生成镜像，再用 `docker run --rm `命令运行镜像

## 本地生成native应用
### 预装 GravvlJVM

```shell
 sdk install java 21.0.2-graalce
 ```

 ## 生成native应用

 ```bash
 ./gradlew nativeCompile
 ```

 ## 运行native应用

 ```bash
 ./gradlew nativeRun
 ```

 ## 启动时间对比

 - jar启动

    ```text
    2025-12-05T05:21:50.964Z  INFO 18235 --- [springboot4-preview-playground] [           main] .Springboot4PreviewPlaygroundApplication : Started Springboot4PreviewPlaygroundApplication in 1.205 seconds (process running for 1.489) 
    ```

- native应用启动

    ```text
    2025-12-05T05:21:29.530Z  INFO 17913 --- [springboot4-preview-playground] [           main] .Springboot4PreviewPlaygroundApplication : Started Springboot4PreviewPlaygroundApplication in 0.059 seconds (process running for 0.064)
    ```