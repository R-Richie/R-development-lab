package com.r.travel.flow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class CFlowFactory implements FlowFactory{
    @Autowired
    private FlowRouterSpec flowRouterSpec;
    @PostConstruct
    public void init(){
        registerRouter();
    }


    @Override
    public void registerRouter() {
        String routerKey = "c";
        flowRouterSpec.registerRoute(routerKey, getFlow());
    }

    @Override
    public IntegrationFlow getFlow() {
        return f-> f.handle(
                (p,v)-> {
                    log.info("dynamic c flow");
                    return p;
                }
        ).logAndReply();
    }


}
