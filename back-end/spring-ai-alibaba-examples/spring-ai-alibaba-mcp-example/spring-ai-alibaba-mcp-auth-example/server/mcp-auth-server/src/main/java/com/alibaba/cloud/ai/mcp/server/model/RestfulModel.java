package com.alibaba.cloud.ai.mcp.server.model;

import org.springframework.http.HttpMethod;

/**
 * @author yingzi
 * @since 2025/6/28
 */

public record RestfulModel(String name, String description, String inputSchema, String url, String method, String path, HttpMethod httpMethod) {

}
