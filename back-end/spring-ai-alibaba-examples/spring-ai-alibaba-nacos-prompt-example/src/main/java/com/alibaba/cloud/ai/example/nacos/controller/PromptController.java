/*
 * Copyright 2024 the original author or authors.
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
 */

package com.alibaba.cloud.ai.example.nacos.controller;

import java.util.Map;

import com.alibaba.cloud.ai.prompt.ConfigurablePromptTemplate;
import com.alibaba.cloud.ai.prompt.ConfigurablePromptTemplateFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : huangzhen
 */

@RestController
@RequestMapping("/nacos")
public class PromptController {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PromptController.class);

    private final ChatClient client;

    private final ConfigurablePromptTemplateFactory promptTemplateFactory;

    public PromptController(
            ChatModel chatModel,
            ConfigurablePromptTemplateFactory promptTemplateFactory
    ) {

        this.client = ChatClient.builder(chatModel).build();
        this.promptTemplateFactory = promptTemplateFactory;
    }

    @GetMapping("/books")
    public Flux<String> generateJoke(
            @RequestParam(value = "author", required = false, defaultValue = "鲁迅") String authorName,
            HttpServletResponse response
    ) {

        // 防止输出乱码
        response.setCharacterEncoding("UTF-8");

        // 使用 nacos 的 prompt tmpl 创建 prompt
        ConfigurablePromptTemplate template = promptTemplateFactory.create(
                "author",
                "please list the three most famous books by this {author}."
        );
        Prompt prompt = template.create(Map.of("author", authorName));
        logger.info("最终构建的 prompt 为：{}", prompt.getContents());

        return client.prompt(prompt)
                .stream()
                .content();
    }

}
