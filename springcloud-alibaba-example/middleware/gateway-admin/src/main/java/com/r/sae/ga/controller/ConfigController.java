/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.r.sae.ga.controller;

import com.r.sae.ga.enums.ConfigGroupEnum;
import com.r.sae.ga.listener.http.HttpLongPollingDataChangedListener;
import com.r.sae.ga.model.dto.AdminResult;
import com.r.sae.ga.model.dto.ConfigData;
import com.r.sae.ga.utils.AdminResultMessage;
import com.r.sae.ga.utils.CommonErrorCode;
import com.google.common.collect.Maps;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * This Controller only when HttpLongPollingDataChangedListener exist, will take effect.
 */
@ConditionalOnBean(HttpLongPollingDataChangedListener.class)
@RestController
@RequestMapping("/configs")
public class ConfigController {
    
    private final HttpLongPollingDataChangedListener longPollingListener;
    
    public ConfigController(final HttpLongPollingDataChangedListener longPollingListener) {
        this.longPollingListener = longPollingListener;
    }
    
    /**
     * Fetch configs shenyu result.
     *
     * @param groupKeys the group keys
     * @return the shenyu result
     */
    @GetMapping("/fetch")
    public AdminResult fetchConfigs(@NotNull final String[] groupKeys) {
        Map<String, ConfigData<?>> result = Maps.newHashMap();
        for (String groupKey : groupKeys) {
            ConfigData<?> data = longPollingListener.fetchConfig(ConfigGroupEnum.valueOf(groupKey));
            result.put(groupKey, data);
        }
        return new AdminResult(CommonErrorCode.SUCCESSFUL, AdminResultMessage.SUCCESS, result);
    }
    
    /**
     * Listener.
     *
     * @param request  the request
     * @param response the response
     */
    @PostMapping(value = "/listener")
    public void listener(final HttpServletRequest request, final HttpServletResponse response) {
        longPollingListener.doLongPolling(request, response);
    }
    
}
