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

package com.alibaba.cloud.ai.graph.nodes;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import lombok.extern.slf4j.Slf4j;


import java.util.Map;
import java.util.Set;

/**
 * @author yHong
 * @version 1.0
 * @since 2025/6/18 15:58
 */
@Slf4j
public class SensitiveWordDecNode implements NodeAction {
    public static final String OUTPUT_KEY = "is_sensitive";

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        Set<String> black = Set.of("暴力", "中国民主党");
        return Map.of(OUTPUT_KEY, black.contains(state.value("field").orElse("")) ? "yes" : "no");
    }
}
