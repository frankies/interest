/*
 * Copyright 2025-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author brianxiadong
 */
package com.alibaba.cloud.ai.example.mcp.streamable.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.WebClientStreamableHttpTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class McpConfig {
    @Value("${spring.ai.mcp.client.streamable.connections.server1.url}")
    private String mcpServerUrl;

    /**
     * a streamable http transport
     * @param objectMapper
     * @return
     */
    @Bean
    public WebClientStreamableHttpTransport mcpTransport(ObjectMapper objectMapper) {
        return WebClientStreamableHttpTransport.builder(WebClient.builder())
                .endpoint(mcpServerUrl)
                .resumableStreams(true)
                .objectMapper(objectMapper)
                .openConnectionOnStartup(true)
                .build();
    }

    @Bean
    public McpAsyncClient mcpAsyncClient(WebClientStreamableHttpTransport transport) {
        return McpClient.async(transport).build();
    }

    /**
     * Corresponding mcp python sdk :
     * examples/servers/simple-streamablehttp-stateless/mcp_simple_streamablehttp_stateless/server.py
     */
    @Bean
    public McpSchema.Tool startNotificationTool() {
        // Maintain consistency py server list_tools
        String inputSchema = """
                    {
                      "type": "object",
                      "required": ["interval", "count", "caller"],
                      "properties": {
                        "interval": { "type": "number", "description": "Interval between notifications in seconds" },
                        "count": { "type": "number", "description": "Number of notifications to send" },
                        "caller": { "type": "string", "description": "Identifier of the caller to include in notifications" }
                      }
                    }
                """;
        return McpSchema.Tool.builder()
                .name("start-notification-stream")
                .description("Sends a stream of notifications with configurable count and interval")
                .inputSchema(inputSchema)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
