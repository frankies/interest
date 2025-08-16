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

package com.alibaba.cloud.ai.graph.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.alibaba.cloud.ai.graph.server.entity.Field;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author HY-love-sleep
 * @since 2025-04-15
 */
public interface IFieldService extends IService<Field> {
    Field getFieldByName(String name);
}
