package com.R.sae.gateway.syncdata.nacos;

import com.R.sae.gateway.dynamic.GatewayInMemoryService;
import com.R.sae.gateway.syncdata.RouteDataSubscriber;
import org.springframework.cloud.gateway.route.RouteDefinition;

public class InMemoryRouteDataSubscriber implements RouteDataSubscriber {
    private final GatewayInMemoryService gatewayInMemoryService;

    public InMemoryRouteDataSubscriber(GatewayInMemoryService gatewayInMemoryService) {
        this.gatewayInMemoryService = gatewayInMemoryService;
    }

    @Override
    public void onSubscribe(RouteDefinition routeDefinition) {
        gatewayInMemoryService.updateRoute(routeDefinition);
    }

    @Override
    public void unSubscribe(RouteDefinition routeDefinition) {
        gatewayInMemoryService.deleteRoute(routeDefinition.getId());
    }

    @Override
    public void refreshRouteAll() {
        gatewayInMemoryService.clearAllRoutes();
    }
}
