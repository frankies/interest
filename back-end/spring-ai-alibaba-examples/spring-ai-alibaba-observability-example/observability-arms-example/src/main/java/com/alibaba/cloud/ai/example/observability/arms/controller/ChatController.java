package com.alibaba.cloud.ai.example.observability.arms.controller;

import java.util.Map;

import io.opentelemetry.api.trace.Span;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 */

@RestController
public class ChatController {

	private final ChatClient chatClient;

	public ChatController(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}

	@GetMapping("/joke")
	public Map<String, String> joke() {

		var reply = chatClient
				.prompt()
				.user("Tell me a joke. be concise. don't send anything except the joke.")
				.call()
				.content();

		Span currentSpan = Span.current();

		return Map.of("joke", reply, "traceId", currentSpan.getSpanContext().getTraceId());
	}

}
