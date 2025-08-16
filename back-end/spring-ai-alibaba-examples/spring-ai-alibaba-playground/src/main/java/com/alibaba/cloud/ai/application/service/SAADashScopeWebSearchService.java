package com.alibaba.cloud.ai.application.service;

import com.alibaba.cloud.ai.application.entity.dashscope.ChatResponseDTO;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 */

@Service("dashscopeWebSearchServiceImpl")
public class SAADashScopeWebSearchService implements ISAAWebSearchService{

    private final ChatClient chatClient;

    private final SimpleLoggerAdvisor simpleLoggerAdvisor;

    public SAADashScopeWebSearchService(
            SimpleLoggerAdvisor simpleLoggerAdvisor,
            @Qualifier("dashscopeChatModel")ChatModel chatModel)
    {
        this.simpleLoggerAdvisor = simpleLoggerAdvisor;
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(simpleLoggerAdvisor)
                .build();
    }

    public Flux<ChatResponseDTO> chat(String prompt) {
        var searchOptions = DashScopeApi.SearchOptions.builder()
                .forcedSearch(true)
                .enableSource(true)
                .searchStrategy("pro")
                .enableCitation(true)
                .citationFormat("[<number>]")
                .build();

        var options = DashScopeChatOptions.builder()
                .withEnableSearch(true)
                .withModel(DashScopeApi.ChatModel.DEEPSEEK_V3.getValue())
                .withSearchOptions(searchOptions)
                .withTemperature(0.7)
                .build();

        // todo: 优化下直接用 stream 返回
        return Flux.defer(() -> {
            // Call the chat client and retrieve the chat response
            ChatResponse chatResponse = this.chatClient
                    .prompt(new Prompt(prompt, options))
                    .call()
                    .chatResponse();

            // Extract the result and metadata
            String llmRes = chatResponse.getResult().getOutput().getText();
            var searchInfo = chatResponse.getResult().getOutput().getMetadata().get("search_info");

            // Create and return a new ChatResponseDTO object wrapped in a Flux
            return Flux.just(new ChatResponseDTO(llmRes, searchInfo));
        });
    }

}
