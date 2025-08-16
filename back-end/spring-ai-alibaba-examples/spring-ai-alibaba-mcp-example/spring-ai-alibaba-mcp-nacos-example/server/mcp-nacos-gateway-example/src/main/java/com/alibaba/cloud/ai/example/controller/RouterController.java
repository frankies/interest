package com.alibaba.cloud.ai.example.controller;

import com.alibaba.cloud.ai.mcp.router.service.McpRouterService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouterController {
    
    @Resource
    private McpRouterService mcpRouterService;
    
    
    /**
     * 搜索 MCP 服务 GET /search?taskDescription={taskDescription}&keywords={keywords}&limit={limit}
     */
    @GetMapping("/search")
    public ResponseEntity<String> searchServices(@RequestParam String taskDescription,
                                                              @RequestParam(required = false) String keywords,
                                                              @RequestParam(defaultValue = "10", required = false) int limit) {
        return ResponseEntity.ok(mcpRouterService.searchMcpServer(taskDescription, keywords, limit));
    }

}
