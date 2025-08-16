package com.alibaba.cloud.ai.application.service;

import com.alibaba.cloud.ai.application.entity.dashscope.ChatResponseDTO;
import reactor.core.publisher.Flux;

/**
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 * AI 联网搜索服务：提供两种实现
 * 1. 基于 DashScope 的 Web Search；
 * 2. 基于 Spring AI Module RAG 实现。
 */

public interface ISAAWebSearchService {

    Flux<ChatResponseDTO> chat(String prompt);

}
