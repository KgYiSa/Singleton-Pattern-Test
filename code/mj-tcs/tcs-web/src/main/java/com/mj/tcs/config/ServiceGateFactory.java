//package com.mj.tcs.config;
//
////import com.mj.tcs.service.ServiceGateway;
//
//import com.mj.tcs.service.ServiceGateway;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//
///**
// * @author Wang Zhen
// */
//@Configuration
//public class ServiceGateFactory {
//    private static ServiceGateway serviceGateway;
//
//
//    @Order(Ordered.LOWEST_PRECEDENCE)
//    @Bean
//    public ServiceGateway serviceGateway() {
//        if (serviceGateway == null) {
//            serviceGateway = new ServiceGateway();
//        }
//
//        return serviceGateway;
//    }
//
//    public static ServiceGateway getServiceGateway() {
//        return serviceGateway;
//    }
//}
