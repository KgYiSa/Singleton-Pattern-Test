package com.mj.tcs.api.web;

import com.mj.tcs.service.ServiceGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * @author Wang Zhen
 */
@Controller
public class ServiceController {

    @Autowired
    private SimpMessagingTemplate messageTemplate;// For client messaging

    @Autowired
    private ServiceGateway serviceGateway;

    public ServiceGateway getService() {
        return this.serviceGateway;
    }

    public SimpMessagingTemplate getMessageSender() {
        return this.messageTemplate;
    }
}
