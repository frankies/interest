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
 * @author yHong
 */

package com.alibaba.example.controller;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author yHong
 * @version 1.0
 * @since 2025/4/24 16:07
 */
@RestController
@RequestMapping("/write")
public class WritingAssistantController {

	private final CompiledGraph compiledGraph;

	@Autowired
	public WritingAssistantController(@Qualifier("writingAssistantGraph") StateGraph writingAssistantGraph)
			throws GraphStateException {
		this.compiledGraph = writingAssistantGraph.compile();
	}

	/**
	 * 调用写作助手流程图 示例请求：GET /write?text=今天我去了西湖，天气特别好，感觉特别开心
	 */
	@GetMapping
	public Map<String, Object> write(@RequestParam("text") String inputText) throws GraphRunnerException {
		var resultFuture = compiledGraph.invoke(Map.of("original_text", inputText));
		var result = resultFuture.get();
		return result.data();
	}

}
