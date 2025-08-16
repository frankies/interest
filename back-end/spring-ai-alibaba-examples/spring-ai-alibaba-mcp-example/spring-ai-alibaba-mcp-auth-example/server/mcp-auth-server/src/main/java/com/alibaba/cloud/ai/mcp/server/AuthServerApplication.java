package com.alibaba.cloud.ai.mcp.server;

import com.alibaba.cloud.ai.mcp.server.parse.ParseRestful;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author yingzi
 * @since 2025/6/28
 */
@SpringBootApplication
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider mcpRestfulToolCallbackProvider(ParseRestful parseRestful) {
         return parseRestful.getRestfulToolCallbackProvider();
    }
}
