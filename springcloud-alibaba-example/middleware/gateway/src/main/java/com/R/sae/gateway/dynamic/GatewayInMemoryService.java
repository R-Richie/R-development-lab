package com.R.sae.gateway.dynamic;

import com.alibaba.nacos.api.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
@Service
@Slf4j
public class GatewayInMemoryService {
    private final RouteDefinitionWriter routeDefinitionWriter;

    private final RouteDefinitionLocator routeDefinitionLocator;

    private final ApplicationEventPublisher publisher;

    public GatewayInMemoryService(RouteDefinitionWriter routeDefinitionWriter,
                                  RouteDefinitionLocator routeDefinitionLocator,
                                  ApplicationEventPublisher publisher) {
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.routeDefinitionLocator = routeDefinitionLocator;
        this.publisher = publisher;
    }

    public void updateRoute(RouteDefinition route){
        if(ObjectUtils.isEmpty(route)){
            log.info("No routes found");
            return;
        }
        routeDefinitionWriter.save(Mono.just(route)).subscribe();
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }
    public void updateRoutes(List<RouteDefinition> routes){
        if(CollectionUtils.isEmpty(routes)){
            log.info("No routes found");
            return;
        }
        routes.forEach( r -> {
            routeDefinitionWriter.save(Mono.just(r)).subscribe();
            publisher.publishEvent(new RefreshRoutesEvent(this));
        });
    }
    public void refreshAllRoutes(List<RouteDefinition> routes){
        log.info("gateway refresh all route;[{}]", routes);
        List<RouteDefinition> routeDefinitionExists = routeDefinitionLocator
                .getRouteDefinitions()
                .buffer()
                .blockFirst();
        if(!CollectionUtils.isEmpty(routeDefinitionExists)){
            routeDefinitionExists.forEach(rd -> {
                deleteRoute(rd.getId());
            });

        }
        updateRoutes(routes);
    }
    public void clearAllRoutes(){
        List<RouteDefinition> routeDefinitionsExists = routeDefinitionLocator
                .getRouteDefinitions()
                .buffer()
                .blockFirst();
        if(!CollectionUtils.isEmpty(routeDefinitionsExists)){
            routeDefinitionsExists.forEach(rd -> {
                deleteRoute(rd.getId());
            });
        }
    }
    public void deleteRoute(String id){
        if(StringUtils.isBlank(id)){
            log.error("No route id for delete");
            return;
        }
        routeDefinitionLocator.getRouteDefinitions()
                .filterWhen(route -> Flux.just(StringUtils.equals(id, route.getId())))
                .subscribe(
                        (x)->{
                            routeDefinitionWriter.delete(Mono.just(id)).subscribe();
                            publisher.publishEvent(new RefreshRoutesEvent(this));
                        }
                );

    }
}
