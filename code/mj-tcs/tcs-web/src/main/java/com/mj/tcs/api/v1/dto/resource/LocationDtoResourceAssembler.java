///*******************************************************************************
// * mj-tcs copyright information:
// * Copyright (c) 2015 Shanghai MJ Intelligent System Co.,Ltd
// * All rights reserved.
// ******************************************************************************/
//
//package com.mj.tcs.api.v1.dto.resource;
//
//import com.mj.tcs.api.v1.dto.LocationDto;
//import com.mj.tcs.api.v1.web.LocationController;
//import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
//
//
///**
// * @author Wang Zhen
// */
//public class LocationDtoResourceAssembler extends ResourceAssemblerSupport<LocationDto, LocationDtoResource> {
//    public LocationDtoResourceAssembler() {
//        super(LocationController.class, LocationDtoResource.class);
//    }
//
//    @Override
//    public LocationDtoResource toResource(LocationDto location) {
//        if (location == null) {
//            throw new NullPointerException("LocationDtoResourceAssembler: location is null!");
//        }
//        LocationDtoResource resource = createResourceWithId(location.getId(), location);
//        return resource;
//    }
//
//    @Override
//    protected LocationDtoResource instantiateResource(LocationDto entity) {
//        return new LocationDtoResource(entity);
//    }
//}