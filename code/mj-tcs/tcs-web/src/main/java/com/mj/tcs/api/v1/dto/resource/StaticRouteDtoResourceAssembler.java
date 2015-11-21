///*******************************************************************************
// * mj-tcs copyright information:
// * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
// * All rights reserved.
// ******************************************************************************/
//
//package com.mj.tcs.api.v1.dto.resource;
//
//
//import com.mj.tcs.api.v1.dto.StaticRouteDto;
//import com.mj.tcs.api.v1.web.StaticRouteController;
//import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
//
///**
// * @author Wang Zhen
// */
//public class StaticRouteDtoResourceAssembler extends ResourceAssemblerSupport<StaticRouteDto, StaticRouteDtoResource> {
//    public StaticRouteDtoResourceAssembler() {
//        super(StaticRouteController.class, StaticRouteDtoResource.class);
//    }
//
//    @Override
//    public StaticRouteDtoResource toResource(StaticRouteDto staticRoute) {
//        if (staticRoute == null) {
//            throw new NullPointerException("StaticRouteDtoResourceAssembler: staticRoute is null!");
//        }
//        StaticRouteDtoResource resource = createResourceWithId(staticRoute.getId(), staticRoute);
//        return resource;
//    }
//
//    @Override
//    protected StaticRouteDtoResource instantiateResource(StaticRouteDto entity) {
//        return new StaticRouteDtoResource(entity);
//    }
//}