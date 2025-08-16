/*
 * Copyright 2025 the original author or authors.
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

package com.alibaba.cloud.ai.observationhandlerexample.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.observationhandlerexample.observationHandler.CustomerObservationHandler;
import io.micrometer.observation.ObservationRegistry;


import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: XiaoYunTao
 * @Date: 2024/12/31
 */
@RestController
@RequestMapping("/custom/observation/chat")
public class ChatModelController {

    private final DashScopeChatModel dashScopeChatModel;

    public ChatModelController(Environment environment, ObservationRegistry observationRegistry) {
        observationRegistry.observationConfig().observationHandler(new CustomerObservationHandler()) ;
        String dashscopeApiKey = environment.getProperty("spring.ai.dashscope.api-key");
        this.dashScopeChatModel = DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder().apiKey(dashscopeApiKey).build())
                .observationRegistry(observationRegistry)
                .build();
    }


    @GetMapping
    public String chat(@RequestParam(name = "message", defaultValue = "hi") String message) {
        return dashScopeChatModel.call(message);
    }

}
