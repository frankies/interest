package com.alibaba.cloud.ai.example.observability.controller;

import java.util.List;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 */

@RestController
@RequestMapping("/observability/embedding")
public class EmbeddingModelController {

	private final EmbeddingModel embeddingModel;

	public EmbeddingModelController(EmbeddingModel embeddingModel) {
		this.embeddingModel = embeddingModel;
	}

	@GetMapping
	public String embedding() {

		var embeddings = embeddingModel.embed("hello world.");
		return "embedding vector size:" + embeddings.length;
	}

	@GetMapping("/generic")
	public String embeddingGenericOpts() {

		var embeddings = embeddingModel.call(new EmbeddingRequest(
				List.of("hello world."),
				DashScopeEmbeddingOptions.builder().withModel(DashScopeApi.EmbeddingModel.EMBEDDING_V3.getValue()).build())
		).getResult().getOutput();
		return "embedding vector size:" + embeddings.length;
	}

}
