package com.r.sae.gateway.syncdata.nacos;

import com.r.sae.gateway.constant.NacosPathConstants;
import com.r.sae.gateway.dto.AppAuthData;
import com.r.sae.gateway.syncdata.AuthDataSubscriber;
import com.r.sae.gateway.syncdata.RouteDataSubscriber;
import com.r.sae.gateway.utils.JacksonUtils;
import com.r.sae.gateway.utils.MapUtils;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.*;
import java.util.concurrent.Executor;

@Slf4j
public class NacosCacheHandler {
    protected static final Map<String, List<Listener>> LISTENERS = Maps.newHashMap();

    private final ConfigService configService;

    private final RouteDataSubscriber routeDataSubscribe;

    private final List<AuthDataSubscriber> authDataSubscribers;

    public NacosCacheHandler(ConfigService configService,
                             RouteDataSubscriber routeDataSubscribe,
                             List<AuthDataSubscriber> authDataSubscribers) {
        this.configService = configService;
        this.routeDataSubscribe = routeDataSubscribe;
        this.authDataSubscribers = authDataSubscribers;
    }

    public ConfigService getConfigService() {
        return configService;
    }
    protected  void updateAuthMap(final String configInfo){
        List<AppAuthData> appAuthDataList = Lists.newArrayList();
        try{
            Map<String, AppAuthData> routeDefinitionMap = JacksonUtils.getJson().readValue(configInfo, new TypeReference<Map<String, AppAuthData>>() {
            });
            appAuthDataList.addAll(routeDefinitionMap.values());
        }catch (Exception e){
            log.error("nacos json parse error", e);
        }
        authDataSubscribers.forEach(AuthDataSubscriber::refresh);
        appAuthDataList.forEach (appAuthData-> authDataSubscribers.forEach(
                subscriber-> {
                subscriber.unSubscribe(appAuthData);
                subscriber.onSubscribe(appAuthData);
            }));

    }

    protected void updateRouteMap(final String configInfo){
        List<RouteDefinition> routeDefinitions = Lists.newArrayList();
        try{
            Map<String, RouteDefinition> routeDefinitionMap = JacksonUtils.getJson()
                    .readValue(configInfo, new TypeReference<Map<String, RouteDefinition>>() {
            });
            routeDefinitions.addAll(routeDefinitionMap.values());
        }catch (Exception e){
            log.error("nacos json parse error", e);
        }
        Optional.of(routeDataSubscribe).ifPresent((RouteDataSubscriber::refreshRouteAll));
        routeDefinitions.forEach(routeData ->
                Optional.ofNullable(routeDataSubscribe)
                        .ifPresent(subscriber -> {
                            subscriber.unSubscribe(routeData);
                            subscriber.onSubscribe(routeData);
                        }));
    }
    private String getConfigAndSignListener(final String dataId, final Listener listener){
        String config = null;
        try{
            config = configService.getConfigAndSignListener(dataId, NacosPathConstants.GROUP,
                    NacosPathConstants.DEFAULT_TIME_OUT, listener);
        }catch (NacosException e){
            log.error("nacos listener error",e);
        }
        if(Objects.isNull(config)){
            config = NacosPathConstants.EMPTY_CONFIG_DEFAULT_VALUE;
        }
        return config;
    }
    protected void watcherData(final String dataId, final OnChange oc){
        Listener listener = new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                oc.change(configInfo);
            }
        };
        oc.change(getConfigAndSignListener(dataId, listener));
        MapUtils.computeIfAbsent(LISTENERS, dataId, key -> new ArrayList<>()).add(listener);
    }

    protected  interface  OnChange{
        void change(String changeData);
    }

}
