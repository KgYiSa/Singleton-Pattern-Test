package com.mj.tcs.service.modelling;

import com.mj.tcs.exception.TcsServerRuntimeException;
import com.mj.tcs.data.model.LocationType;
import com.mj.tcs.data.model.Scene;
import com.mj.tcs.data.base.BaseEntity;
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
public class LocationTypeService implements IEntityService {

    @Autowired
    private LocationTypeRepository locationTypeRepository;

    @Override
    public boolean canSupportEntityClass(Class entityClass) {
        if (LocationType.class.equals(entityClass)) {
            return true;
        }

        return false;
    }

    @Override
    public Collection<Object> get(ServiceGetParams params) {

        switch (params.getType()) {
            case GET_ONE_BY_ELEMENT_ID:
                LocationType entity = locationTypeRepository.findOne((long) params.getParameter(ServiceGetParams.NAME_ELEMENT_ID));
                return Arrays.asList(entity);
            case GET_ALL_BY_SCENE_ID:
                return (Collection) locationTypeRepository.findAllByScene((long) params.getParameter(ServiceGetParams.NAME_SCENE_ID));
            case GET_ALL:
                return (Collection) locationTypeRepository.findAll();
            default:
                throw new IllegalArgumentException("parameters in locationType service is incorrect: " + params);
        }
    }

    @Override
    public LocationType create(BaseEntity entity) {
        throw new TcsServerRuntimeException("Not supported, please create from scene then save it");
    }

    @Override
    public LocationType update(BaseEntity entity) {
        LocationType locationType = (LocationType) Objects.requireNonNull(entity, "location type entity is null");

        Scene scene = locationType.getScene();

        final long id = locationType.getId();
        if (scene.getLocationById(id) == null) {
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
