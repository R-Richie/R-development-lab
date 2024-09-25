package com.r.travel.controller;

import cn.hutool.core.lang.hash.Hash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/flow")
public class FlowController {

    @Resource(name = "hotelFulfillmentChannel")
    private MessageChannel hotelFulfillmentChannel;




    @PostMapping(value = "/hotelFulfillment")
    public void triggerHotelChannel() {

        Map<String, Object> headerMap = new HashMap<>();
//        headerMap.put(HotelFlowConstants.SYNC_CALLBACK_PROVIDER, ProviderEnum.GREEN_CLOUD.getCode());
        MessageHeaders messageHeaders = new MessageHeaders(headerMap);

        Message<String> message = MessageBuilder.createMessage("666", messageHeaders);
        hotelFulfillmentChannel.send(message);
//        return GreenCloudResponseType.builder()
//                .resultCode(0)
//                .build();
    }
}
