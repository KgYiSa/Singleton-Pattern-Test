//package com.mj.tcs.api.dto.resource;
//
//
//import com.mj.tcs.api.dto.PathDto;
//import com.mj.tcs.api.web.PathController;
//import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
//
///**
// * @author Wang Zhen
// */
//public class PathDtoResourceAssembler extends ResourceAssemblerSupport<PathDto, PathDtoResource> {
//    public PathDtoResourceAssembler() {
//        super(PathController.class, PathDtoResource.class);
//    }
//
//    @Override
//    public PathDtoResource toResource(PathDto path) {
//        if (path == null) {
//            throw new NullPointerException("PathDtoResourceAssembler: path is null!");
//        }
//        PathDtoResource resource = createResourceWithId(path.getId(), path);
//        return resource;
//    }
//
//    @Override
//    protected PathDtoResource instantiateResource(PathDto entity) {
//        return new PathDtoResource(entity);
//    }
//}