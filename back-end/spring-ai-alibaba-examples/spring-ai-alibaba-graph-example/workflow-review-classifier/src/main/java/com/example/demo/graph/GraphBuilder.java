package com.example.demo.graph;

import java.util.List;
import java.util.Map;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.GraphRepresentation;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.OverAllStateFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.action.AsyncNodeAction;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.node.HttpNode;
import com.alibaba.cloud.ai.graph.node.QuestionClassifierNode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.alibaba.cloud.ai.graph.StateGraph.END;
import static com.alibaba.cloud.ai.graph.StateGraph.START;
import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;

@Component
public class GraphBuilder {

    @Bean
    public CompiledGraph buildGraph(ChatModel chatModel) throws GraphStateException {
        ChatClient chatClient = ChatClient.builder(chatModel).defaultAdvisors(new SimpleLoggerAdvisor()).build();
        OverAllStateFactory stateFactory = () -> {
            OverAllState overAllState = new OverAllState();
            // build state
            // overAllState.registerKeyAndStrategy("prompt", (o, o2) -> o2);
            overAllState.registerKeyAndStrategy("input", (o1, o2) -> o2);
            overAllState.registerKeyAndStrategy("1711529066687_output", (o1, o2) -> o2);
            overAllState.registerKeyAndStrategy("17440815773820_output", (o1, o2) -> o2);
            overAllState.registerKeyAndStrategy("1711529036587_output", (o1, o2) -> o2);
            overAllState.registerKeyAndStrategy("1711529077513_output", (o1, o2) -> o2);

            return overAllState;
        };

        StateGraph stateGraph = new StateGraph(stateFactory);
        // add nodes
        // —— QuestionClassifierNode [1711529036587] ——
        QuestionClassifierNode questionClassifier1 = QuestionClassifierNode.builder()
            .chatClient(chatClient)
            .inputTextKey("input")
            .categories(List.of("positive feedback", "negative feedback"))
            .outputKey("1711529036587_output")
            .classificationInstructions(List.of("请根据输入内容选择对应分类"))
            .build();
        stateGraph.addNode("1711529036587", AsyncNodeAction.node_async(questionClassifier1));

        // —— QuestionClassifierNode [1711529066687] ——
        QuestionClassifierNode questionClassifier2 = QuestionClassifierNode.builder()
            .chatClient(chatClient)
            .inputTextKey("input")
            .categories(List.of("after-sale service", "product quality"))
            .outputKey("1711529066687_output")
            .classificationInstructions(List.of("请根据输入内容选择对应分类"))
            .build();
        stateGraph.addNode("1711529066687", AsyncNodeAction.node_async(questionClassifier2));

        // —— HttpNode [1711529077513] ——
        HttpNode http1 = HttpNode.builder()
                .url("http://47.83.24.236:38080/negative")
                .header("Content-Type", "application/json")
                .retryConfig(new HttpNode.RetryConfig(3, 100, true))
                .outputKey("1711529077513_output")
                .build();
        stateGraph.addNode("1711529077513", AsyncNodeAction.node_async(http1));

        // —— HttpNode [17440815773820] ——
        HttpNode http2 = HttpNode.builder()
                .url("http://47.83.24.236:38080/positive")
                .header("Content-Type", "application/json")
                .retryConfig(new HttpNode.RetryConfig(3, 100, true))
                .outputKey("17440815773820_output")
                .build();
        stateGraph.addNode("17440815773820", AsyncNodeAction.node_async(http2));


        // add edges
        stateGraph.addEdge(START, "1711529036587");
        stateGraph.addEdge("1711529077513", END);
        stateGraph.addEdge("17440815773820", END);
        stateGraph.addConditionalEdges("1711529036587",
            edge_async(state -> {
                String value = state.value("1711529036587_output", String.class).orElse("");
            	if (value.contains("negative feedback")) return "negative feedback";
            	if (value.contains("positive feedback")) return "positive feedback";
                return null;
            }),
            Map.of("negative feedback", "1711529066687", "positive feedback", "17440815773820")
        );
        stateGraph.addConditionalEdges("1711529066687",
            edge_async(state -> {
                String value = state.value("1711529066687_output", String.class).orElse("");
            	if (value.contains("after-sale service")) return "after-sale service";
                if (value.contains("product quality")) return "product quality";
                return null;
            }),
            Map.of("after-sale service", "1711529077513", "product quality", "1711529077513")
        );

        printGraphImage(stateGraph);

        return stateGraph.compile();
    }

    private static void printGraphImage(StateGraph stateGraph) {
        GraphRepresentation graphRepresentation = stateGraph.getGraph(GraphRepresentation.Type.PLANTUML,
                "workflow graph");
        System.out.println("\n\n");
        System.out.println(graphRepresentation.content());
        System.out.println("\n\n");
    }
}
