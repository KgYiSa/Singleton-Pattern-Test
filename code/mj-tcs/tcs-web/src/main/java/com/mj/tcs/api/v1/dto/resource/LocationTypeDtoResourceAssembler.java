//package com.mj.tcs.api.v1.dto.resource;
//
//import com.mj.tcs.api.v1.dto.LocationTypeDto;
//import com.mj.tcs.api.v1.web.LocationTypeController;
//import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
//
///**
// * @author Wang Zhen
// */
//public class LocationTypeDtoResourceAssembler extends ResourceAssemblerSupport<LocationTypeDto, LocationTypeDtoResource> {
//    public LocationTypeDtoResourceAssembler() {
//        super(LocationTypeController.class, LocationTypeDtoResource.class);
//    }
//
//    @Override
//    public LocationTypeDtoResource toResource(LocationTypeDto dto) {
//        if (dto == null) {
//            throw new NullPointerException("LocationTypeDtoResourceAssembler: dto is null!");
//        }
//        LocationTypeDtoResource resource = createResourceWithId(dto.getId(), dto);
//        return resource;
//    }
//
//    @Override
//    protected LocationTypeDtoResource instantiateResource(LocationTypeDto entity) {
//        return new LocationTypeDtoResource(entity);
//    }
//}