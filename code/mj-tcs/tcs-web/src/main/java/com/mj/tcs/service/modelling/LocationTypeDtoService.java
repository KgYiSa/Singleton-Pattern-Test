package com.mj.tcs.service.modelling;

import com.mj.tcs.api.v1.dto.LocationTypeDto;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.exception.TcsServerRuntimeException;
import com.mj.tcs.repository.LocationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@Component
public class LocationTypeDtoService implements IEntityDtoService {

    @Autowired
    private LocationTypeRepository locationTypeRepository;

    @Override
    public boolean canSupportEntityClass(Class entityClass) {
        if (LocationTypeDto.class.equals(entityClass)) {
            return true;
        }

        return false;
    }

    @Override
    public Collection<Object> get(ServiceGetParams params) {

        switch (params.getType()) {
            case GET_ONE_BY_ELEMENT_ID:
                LocationTypeDto entity = locationTypeRepository.findOne((long) params.getParameter(ServiceGetParams.NAME_ELEMENT_ID));
                return Arrays.asList(entity);
            case GET_ALL_BY_SCENE_ID:
                return (Collection) locationTypeRepository.findAllBySceneDto((long) params.getParameter(ServiceGetParams.NAME_SCENE_ID));
            case GET_ALL:
                return (Collection) locationTypeRepository.findAll();
            default:
                throw new IllegalArgumentException("parameters in locationType service is incorrect: " + params);
        }
    }

    @Override
    public LocationTypeDto create(BaseEntityDto entity) {
        throw new TcsServerRuntimeException("Not supported, please create from scene then save it");
    }

    @Override
    public LocationTypeDto update(BaseEntityDto entity) {
        LocationTypeDto locationType = (LocationTypeDto) Objects.requireNonNull(entity, "location type entity is null");

        SceneDto scene = locationType.getSceneDto();

        final long id = locationType.getId();
        if (scene.getLocationDtoById(id) == null) {
            throw new TcsServerRuntimeException("The location type is not belonging to the scene " +
                    "or you can not create it by the method");
        }

        return locationTypeRepository.save(locationType);
    }

    @Override
    public void delete(Long id) {
        throw new TcsServerRuntimeException("Not supported, please delete from scene then save it");
    }
}
