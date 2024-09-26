package com.r.travel.flow;

import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.RouterSpec;
import org.springframework.integration.router.ExpressionEvaluatingRouter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
public class FlowRouterSpec {
    private static final String DEFAULT_FLOW = "default";
    private final Map<String, IntegrationFlow> fulfillmentTryOrderFlowMappings = new ConcurrentHashMap<>();

    public FlowRouterSpec(IntegrationFlow defaultFlow) {
        fulfillmentTryOrderFlowMappings.put(DEFAULT_FLOW, defaultFlow);

    }
    public void registerRoute(String routeKey, IntegrationFlow subFlow) {
        fulfillmentTryOrderFlowMappings.put(routeKey, subFlow);
    }
    // 获取注册的RouterSpec供其他地方使用
    public Consumer<RouterSpec<String, ExpressionEvaluatingRouter>> getRouterSpec() {
        return spec -> {
            fulfillmentTryOrderFlowMappings.forEach((key, flow) ->
                    spec.subFlowMapping(key, sf -> sf.gateway(flow)));

            // 可选：设置默认路由
            spec.defaultSubFlowMapping(sf -> sf.gateway(fulfillmentTryOrderFlowMappings.get(DEFAULT_FLOW)));
        };
    }
}
