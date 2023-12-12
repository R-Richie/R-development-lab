package com.r.sae.gateway.syncdata;

import org.springframework.cloud.gateway.route.RouteDefinition;

public interface RouteDataSubscriber {
    default void onSubscribe(RouteDefinition routeDefinition){}

    default void unSubscribe(RouteDefinition routeDefinition){}

    default void refreshRouteAll(){

    }
}
