package com.alibaba.cloud.ai.graph.react;

import com.alibaba.cloud.ai.graph.react.function.WeatherProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(WeatherProperties.class)
public class ReactApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactApplication.class, args);
    }

}
