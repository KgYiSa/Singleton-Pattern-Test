///*******************************************************************************
// * mj-tcs copyright information:
// * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
// * All rights reserved.
// ******************************************************************************/
//
//package com.mj.tcs.api.dto.resource;
//
//
//import com.mj.tcs.api.dto.TransportOrderDto;
//import com.mj.tcs.api.web.TransportOrderController;
//import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
//
///**
// * @author Wang Zhen
// */
//public class TransportOrderDtoResourceAssembler extends ResourceAssemblerSupport<TransportOrderDto, TransportOrderDtoResource> {
//    public TransportOrderDtoResourceAssembler() {
//        super(TransportOrderController.class, TransportOrderDtoResource.class);
//    }
//
//    @Override
//    public TransportOrderDtoResource toResource(TransportOrderDto transportOrder) {
//        if (transportOrder == null) {
//            throw new NullPointerException("transportOrderDtoResourceAssembler: transportOrder is null!");
//        }
//        TransportOrderDtoResource resource = createResourceWithId(transportOrder.getId(), transportOrder);
//        return resource;
//    }
//
//    @Override
//    protected TransportOrderDtoResource instantiateResource(TransportOrderDto entity) {
//        return new TransportOrderDtoResource(entity);
//    }
//}