package com.mj.tcs.api.v1.dto.resource;


import com.mj.tcs.api.v1.dto.VehicleDto;
import com.mj.tcs.api.v1.web.VehicleController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

/**
 * @author Wang Zhen
 */
public class VehicleDtoResourceAssembler extends ResourceAssemblerSupport<VehicleDto, VehicleDtoResource> {
    public VehicleDtoResourceAssembler() {
        super(VehicleController.class, VehicleDtoResource.class);
    }

    @Override
    public VehicleDtoResource toResource(VehicleDto vehicle) {
        if (vehicle == null) {
            throw new NullPointerException("vehicleDtoResourceAssembler: vehicle is null!");
        }
        VehicleDtoResource resource = createResourceWithId(vehicle.getId(), vehicle);
        return resource;
    }

    @Override
    protected VehicleDtoResource instantiateResource(VehicleDto entity) {
        return new VehicleDtoResource(entity);
    }
}