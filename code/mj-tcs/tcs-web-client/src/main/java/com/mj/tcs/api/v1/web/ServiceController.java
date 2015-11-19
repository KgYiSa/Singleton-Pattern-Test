package com.mj.tcs.api.v1.web;

import com.mj.tcs.service.ServiceGateway;
import com.mj.tcs.service.ServiceStateModelling;
import com.mj.tcs.service.ServiceStateOperating;
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

    public ServiceStateModelling getModellingService() {
        return this.serviceGateway.getModellingService();
    }

    public ServiceStateOperating getOperatingService() {
        return this.serviceGateway.getOperatingService();
    }

    public SimpMessagingTemplate getMessageSender() {
        return this.messageTemplate;
    }
}
