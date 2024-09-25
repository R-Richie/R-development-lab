package com.r.travel.flow;

import com.r.travel.constants.MessageFlowConstants;
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

import static com.r.travel.constants.MessageFlowConstants.SPEL_HEADERS;

@Component
public class HotelFulfillmentFlow {
    @Bean("hotelFulfillmentChannel")
    public MessageChannel initChannel() {
        return MessageChannels.direct().get();
    }
    @Bean
    public MessageChannel hotelOutChannel() {
        return MessageChannels.direct().get();
    }
    @Bean
    public IntegrationFlow hotelFulfillmentSubFlowDefinition() {
        //业务幂等性判断
        return IntegrationFlows.from("hotelFulfillmentChannel")
                .enrichHeaders(h -> h.messageProcessor(payloadAddHeader("test")))
                .channel("hotelOutChannel")
                .logAndReply(LoggingHandler.Level.INFO, SPEL_HEADERS);
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
                .log(LoggingHandler.Level.INFO, MessageFlowConstants.SPEL_HEADERS)
                .get();
    }
}
