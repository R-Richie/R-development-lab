package com.r.travel.flow;

import org.springframework.integration.dsl.IntegrationFlow;

public interface FlowFactory {
    void registerRouter();

    IntegrationFlow getFlow();
}
