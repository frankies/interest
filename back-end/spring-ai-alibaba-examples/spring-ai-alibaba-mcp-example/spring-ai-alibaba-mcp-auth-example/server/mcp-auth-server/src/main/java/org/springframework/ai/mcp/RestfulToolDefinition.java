package org.springframework.ai.mcp;

import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

/**
 * @author yingzi
 * @since 2025/6/28
 */

public record RestfulToolDefinition(String name, String description, String inputSchema,
                                    String url, String method, String path, HttpMethod httpMethod) implements ToolDefinition {

    public RestfulToolDefinition {
        Assert.hasText(name, "name cannot be null or empty");
        Assert.hasText(description, "description cannot be null or empty");
        Assert.hasText(inputSchema, "inputSchema cannot be null or empty");
        Assert.hasText(url, "url cannot be null or empty");
        Assert.hasText(method, "method cannot be null or empty");
        Assert.hasText(path, "path cannot be null or empty");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private String inputSchema;

        private String url;
        private String method;
        private String path;
        private HttpMethod httpMethod;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder inputSchema(String inputSchema) {
            this.inputSchema = inputSchema;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder httpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public RestfulToolDefinition build() {
            return new RestfulToolDefinition(name, description, inputSchema, url, method, path, httpMethod);
        }
    }
}
