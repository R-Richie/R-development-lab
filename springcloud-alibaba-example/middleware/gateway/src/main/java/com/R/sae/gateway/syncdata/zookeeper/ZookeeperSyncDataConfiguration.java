package com.R.sae.gateway.syncdata.zookeeper;

import com.R.sae.gateway.dynamic.GatewayInMemoryService;
import com.R.sae.gateway.syncdata.AuthDataSubscriber;
import com.R.sae.gateway.syncdata.RouteDataSubscriber;
import com.R.sae.gateway.syncdata.SignAuthDataSubscriber;
import com.R.sae.gateway.syncdata.SyncDataService;
import com.R.sae.gateway.syncdata.InMemoryRouteDataSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
@ConditionalOnClass(ZookeeperSyncDataService.class)
@ConditionalOnProperty(prefix = "gw.sync.zookeeper", name = "url")
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperSyncDataConfiguration {

    @Bean
    public SyncDataService syncDataService(final ObjectProvider<ZookeeperClient> zookeeperClient,
                                           final ObjectProvider<RouteDataSubscriber> routeSubscriber,
                                            final ObjectProvider<List<AuthDataSubscriber>> authSubscribers) {
        log.info("you use zookeeper sync shenyu data.......");
        return new ZookeeperSyncDataService(zookeeperClient.getIfAvailable(),
                routeSubscriber.getIfAvailable(),
                 authSubscribers.getIfAvailable(Collections::emptyList));
    }
    /**
     * register zkClient in spring ioc.
     *
     * @param zookeeperProps the zookeeper configuration
     * @return ZookeeperClient {@linkplain ZookeeperClient}
     */
    @Bean
    public ZookeeperClient zookeeperClient(final ZookeeperProperties zookeeperProps) {
        int sessionTimeout = Objects.isNull(zookeeperProps.getSessionTimeout()) ? 3000 : zookeeperProps.getSessionTimeout();
        int connectionTimeout = Objects.isNull(zookeeperProps.getConnectionTimeout()) ? 3000 : zookeeperProps.getConnectionTimeout();
        ZookeeperConfig zkConfig = new ZookeeperConfig(zookeeperProps.getUrl());
        zkConfig.setSessionTimeoutMilliseconds(sessionTimeout)
                .setConnectionTimeoutMilliseconds(connectionTimeout);
        ZookeeperClient client = new ZookeeperClient(zkConfig);
        client.start();
        return client;
    }
    @Bean
    public AuthDataSubscriber signAuthDataSubscriber(){
        return new SignAuthDataSubscriber();
    }
    @Bean
    public RouteDataSubscriber inMemoryDataSubscriber(final ObjectProvider<GatewayInMemoryService> gatewayInMemoryServices){
        return new InMemoryRouteDataSubscriber(gatewayInMemoryServices.getIfAvailable());
    }
}
