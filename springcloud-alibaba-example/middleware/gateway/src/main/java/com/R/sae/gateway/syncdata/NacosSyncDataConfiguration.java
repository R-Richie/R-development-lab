package com.R.sae.gateway.syncdata;

import com.R.sae.gateway.dynamic.GatewayInMemoryService;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@ConditionalOnClass(NacosSyncDataService.class)
public class NacosSyncDataConfiguration {
    @Bean
    public SyncDataService nacosSyncDataService(final ObjectProvider<ConfigService> configServices,
                                                final ObjectProvider<RouteDataSubscriber> routeDataSubscribers,
                                                final ObjectProvider<List<AuthDataSubscriber>> authSubscribers){
        return new NacosSyncDataService(configServices.getIfAvailable(),
                routeDataSubscribers.getIfAvailable(),
                authSubscribers.getIfAvailable());
    }
    @Bean
    public AuthDataSubscriber signAuthDataSubscriber(){
        return new SignAuthDataSubscriber();
    }
    @Bean
    public RouteDataSubscriber inMemoryDataSubscriber(final ObjectProvider<GatewayInMemoryService> gatewayInMemoryServices){
        return new InMemoryRouteDataSubscriber(gatewayInMemoryServices.getIfAvailable());
    }
    @Bean
    public ConfigService nacosConfigService(final NacosConfigManager configManager){
        return configManager.getConfigService();
    }

}
