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

package com.alibaba.cloud.ai.graph.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.alibaba.cloud.ai.graph.server.entity.Field;
import com.alibaba.cloud.ai.graph.server.mapper.FieldMapper;
import com.alibaba.cloud.ai.graph.server.service.IFieldService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author HY-love-sleep
 * @since 2025-04-15
 */
@Service
public class FieldServiceImpl extends ServiceImpl<FieldMapper, Field> implements IFieldService {

    @Override
    public Field getFieldByName(String name) {
        LambdaQueryWrapper<Field> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Field::getFieldName, name);
        return this.getOne(lqw);
    }
}
