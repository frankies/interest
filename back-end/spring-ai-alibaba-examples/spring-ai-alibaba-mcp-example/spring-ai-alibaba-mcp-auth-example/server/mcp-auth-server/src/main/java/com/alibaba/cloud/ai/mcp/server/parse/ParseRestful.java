package com.alibaba.cloud.ai.mcp.server.parse;

import com.alibaba.cloud.ai.mcp.server.model.Parameter;
import com.alibaba.cloud.ai.mcp.server.model.RestfulModel;
import com.alibaba.cloud.ai.mcp.server.util.JSONSchemaUtil;
import org.springframework.ai.mcp.McpRestfulToolCallback;
import org.springframework.ai.mcp.McpRestfulToolCallbackProvider;
import org.springframework.ai.mcp.RestfulToolDefinition;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yingzi
 * @since 2025/6/28
 */
@Component
public class ParseRestful {

    public McpRestfulToolCallbackProvider getRestfulToolCallbackProvider() {
        List<McpRestfulToolCallback> toolCallbacks = new ArrayList<>();
        getRestfulModels().forEach(
                restfulModel -> {
                    RestfulToolDefinition restfulToolDefinition = RestfulToolDefinition.builder()
                            .name(restfulModel.name())
                            .description(restfulModel.description())
                            .inputSchema(restfulModel.inputSchema())
                            .url(restfulModel.url())
                            .method(restfulModel.method())
                            .path(restfulModel.path())
                            .httpMethod(restfulModel.httpMethod())
                            .build();
                    McpRestfulToolCallback mcpRestfulToolCallback = McpRestfulToolCallback.builder().toolDefinition(restfulToolDefinition).build();

                    toolCallbacks.add(mcpRestfulToolCallback);
                });
        return McpRestfulToolCallbackProvider.builder()
                .toolCallbacks(toolCallbacks.toArray(new McpRestfulToolCallback[0]))
                .build();
    }


    public List<RestfulModel> getRestfulModels() {

        Parameter parameter = Parameter.builder()
                .parameteNname("timeZoneId")
                .description("time zone id, such as Asia/Shanghai")
                .required(true)
                .type("string")
                .build();
        return List.of(
                new RestfulModel("getCiteTimeMethod", "获取指定时区的时间", JSONSchemaUtil.getInputSchema(List.of(parameter)), "http://localhost:101", "getCiteTimeMethod", "/time/city", HttpMethod.GET)
        );
    }

}
