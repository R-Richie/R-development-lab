package com.r.travel.flow;

import com.r.travel.constants.MessageFlowConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.handler.MessageProcessor;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class Sample1Flow {
    @Bean("hotelFulfillmentChannel")
    public MessageChannel initChannel() {
        return MessageChannels.direct().get();
    }
    @Bean
    public MessageChannel hotelOutChannel() {
        return MessageChannels.direct().get();
    }
    @Bean
    public IntegrationFlow sample1Definition(IntegrationFlow defaultFlow,
                                                             IntegrationFlow aFlow,
                                                             IntegrationFlow bFlow,
                                                             IntegrationFlow cFlow
                                                             ) {
        //业务幂等性判断
        return IntegrationFlows.from("hotelFulfillmentChannel")
                .enrichHeaders(h -> h.messageProcessor(payloadAddHeader("path")))
                .route("headers['path']", m ->
                        m.subFlowMapping("a", sf-> sf.gateway(aFlow))
                                .subFlowMapping("b", sf-> sf.gateway(bFlow))
                                .subFlowMapping("c", sf-> sf.gateway(cFlow))
                                .defaultSubFlowMapping(sf -> sf.gateway(defaultFlow))
                                )

                .channel("hotelOutChannel")
                .get()
                ;
    }

    @Bean
    public IntegrationFlow sampleDynamicDefinition(FlowRouterSpec flowRouterSpec) {
        return IntegrationFlows.from("hotelFulfillmentChannel")
                .enrichHeaders(h -> h.messageProcessor(payloadAddHeader("path")))
                .route("headers['path']", flowRouterSpec.getRouterSpec())
                .channel("hotelOutChannel")
                .get()
                ;
    }

    @Bean
    public IntegrationFlow defaultFlow(){
        return f-> f
                .handle((p,v)-> {
                    log.warn("注意！流程未找到正确分支，走到默认流程中");
                    return p;
                })
                .logAndReply();

    }

    @Bean
    public IntegrationFlow aFlow(){
        return f-> f.handle(
                (p,v)-> {
                    log.info("a flow");
                    return p;
                }
        ).logAndReply();
    }
    @Bean
    public IntegrationFlow bFlow(){
        return f-> f.handle(
                (p,v)-> {
                    log.info("b flow");
                    return p;
                }
        ).logAndReply();
    }
    @Bean
    public IntegrationFlow cFlow(){
        return f-> f.handle(
                (p,v)-> {
                    log.info("c flow");
                    return p;
                }
        ).logAndReply();
    }
    public MessageProcessor<Object> payloadAddHeader(String headerKey)  {
        return  message -> {
            Map<String, Object> header = new HashMap<>();
            header.put(headerKey, message.getPayload());
            return header;
        };
    }
    @Bean
    @DependsOn(value = {"hotelOutChannel"})
    public IntegrationFlow hotelOrderStatusSyncCallbackOutputIntegrationFlow() {
        return IntegrationFlows.from("hotelOutChannel")
                .handle(
                        (p,v)-> {
                            log.info("close");
                            return p;
                        }
                )
                .log(LoggingHandler.Level.INFO, MessageFlowConstants.SPEL_HEADERS)
                .get();
    }
}
