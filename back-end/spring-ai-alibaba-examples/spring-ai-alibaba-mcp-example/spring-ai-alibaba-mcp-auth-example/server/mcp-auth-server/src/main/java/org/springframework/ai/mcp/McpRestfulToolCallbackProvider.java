package org.springframework.ai.mcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.util.Assert;

/**
 * @author yingzi
 * @since 2025/6/28
 */

public class McpRestfulToolCallbackProvider implements ToolCallbackProvider {

    private final ToolCallback[] toolCallbacks;

    private static final Logger logger = LoggerFactory.getLogger(McpRestfulToolCallbackProvider.class);

    public McpRestfulToolCallbackProvider(McpRestfulToolCallback... toolCallbacks) {
        Assert.notNull(toolCallbacks, "toolCallbacks cannot be null");
        this.toolCallbacks = toolCallbacks;
    }

    @Override
    public ToolCallback[] getToolCallbacks() {
        return this.toolCallbacks;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private McpRestfulToolCallback[] toolCallbacks;

        private Builder() {
        }

        public Builder toolCallbacks(McpRestfulToolCallback... toolCallbacks) {
            this.toolCallbacks = toolCallbacks;
            return this;
        }

        public McpRestfulToolCallbackProvider build() {
            return new McpRestfulToolCallbackProvider(this.toolCallbacks);
        }
    }
}
