package com.alibaba.cloud.ai.example.observability.controller;

import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 */

@RestController
@RequestMapping("/observability/tools")
public class ToolCallingController {

	public final ChatClient chatClient;

	public ToolCallingController(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}

	@GetMapping
	public Flux<String> chat(@RequestParam(defaultValue = "how weather in hangzhou?") String prompt) {

		return chatClient.prompt(prompt).toolNames("getWeatherService").stream().content();
	}

}
