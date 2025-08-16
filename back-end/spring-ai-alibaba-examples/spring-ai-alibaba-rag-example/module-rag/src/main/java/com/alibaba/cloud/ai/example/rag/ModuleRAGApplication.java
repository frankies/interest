package com.alibaba.cloud.ai.example.rag;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 */

@SpringBootApplication
public class ModuleRAGApplication {

	public static void main(String[] args) {

		SpringApplication.run(ModuleRAGApplication.class, args);
	}

	@Bean
	public ChatMemory chatMemory() {

		return MessageWindowChatMemory.builder().build();
	}

}
