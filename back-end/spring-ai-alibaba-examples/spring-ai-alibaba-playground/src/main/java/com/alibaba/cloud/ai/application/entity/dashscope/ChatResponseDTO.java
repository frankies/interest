package com.alibaba.cloud.ai.application.entity.dashscope;

/**
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 */

public record ChatResponseDTO(String response, Object searchInfo) {
    public ChatResponseDTO(String response) {
        this(response, null);
    }
}
