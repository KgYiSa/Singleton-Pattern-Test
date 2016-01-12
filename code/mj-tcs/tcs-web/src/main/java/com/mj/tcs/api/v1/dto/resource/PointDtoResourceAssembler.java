//package com.mj.tcs.api.v1.dto.resource;
//
//import com.mj.tcs.api.v1.dto.PointDto;
//import com.mj.tcs.api.v1.web.PointController;
//import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
//
///**
// * @author Wang Zhen
// */
//public class PointDtoResourceAssembler extends ResourceAssemblerSupport<PointDto, PointDtoResource> {
//    public PointDtoResourceAssembler() {
//        super(PointController.class, PointDtoResource.class);
//    }
//
//    @Override
//    public PointDtoResource toResource(PointDto point) {
//        if (point == null) {
//            throw new NullPointerException("PointDtoResourceAssembler: point is null!");
//        }
//        PointDtoResource resource = createResourceWithId(point.getId(), point);
//        return resource;
//    }
//
//    @Override
//    protected PointDtoResource instantiateResource(PointDto entity) {
//        return new PointDtoResource(entity);
//    }
//}