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
 */

package com.alibaba.cloud.ai.graph.tools;

import com.alibaba.cloud.ai.graph.server.entity.Field;
import com.alibaba.cloud.ai.graph.server.service.IFieldService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.DefaultToolDefinition;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.metadata.ToolMetadata;
import org.springframework.stereotype.Component;

@Component
public class FieldSaveTool implements ToolCallback {

    private final IFieldService fieldService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FieldSaveTool(IFieldService fieldService) {
        this.fieldService = fieldService;
    }

    @Override
    public ToolDefinition getToolDefinition() {
        String inputSchema = """
                    {
                      "type": "object",
                      "properties": {
                        "fieldName": {
                          "type": "string",
                          "description": "字段名"
                        },
                        "classification": {
                          "type": "string",
                          "description": "分类路径"
                        },
                        "level": {
                          "type": "integer",
                          "description": "分级"
                        },
                        "reasoning": {
                          "type": "string",
                          "description": "推理过程"
                        }
                      },
                      "required": ["fieldName", "classification", "level", "reasoning"]
                    }
                """;
        return DefaultToolDefinition.builder()
                .name("save_field_classification")
                .description("保存字段分类分级信息")
                .inputSchema(inputSchema)
                .build();
    }

    @Override
    public ToolMetadata getToolMetadata() {
        return ToolCallback.super.getToolMetadata();
    }

    @Override
    public String call(String toolInput) {
        return "";
    }

    @Override
    public String call(String arguments, ToolContext toolContext) {
        try {
            Field field = objectMapper.readValue(arguments, Field.class);
            fieldService.save(field);
            return "字段信息保存成功: " + field.getFieldName();
        } catch (Exception e) {
            return "字段保存失败: " + e.getMessage();
        }
    }
}
