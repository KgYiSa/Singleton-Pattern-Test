package com.mj.tcs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Wang Zhen
 */
@Component
public class ServiceGateway {

    // maintained by spring
    private static ServiceGateway gateway;// = new ServiceGateway();

    @Autowired
    private ServiceStateModelling modellingService;// = new ServiceModelling();

    @Autowired
    private ServiceStateOperating operatingService;// = new ServiceStateOperating();

    public ServiceGateway() {}

    public static ServiceGateway getInstance() {
        if (gateway == null) {
            gateway = new ServiceGateway();
        }

        return gateway;
    }

    public ServiceStateModelling getModellingService() {
        return modellingService;
    }

    public ServiceStateOperating getOperatingService() {
        return operatingService;
    }
}
