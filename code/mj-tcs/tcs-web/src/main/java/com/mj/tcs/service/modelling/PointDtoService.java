package com.mj.tcs.service.modelling;

import com.mj.tcs.api.v1.dto.PointDto;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.exception.TcsServerRuntimeException;
import com.mj.tcs.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@Component
public class PointDtoService implements IEntityDtoService {

    @Autowired
    private PointRepository pointRepository;

    @Override
    public boolean canSupportEntityClass(Class entityClass) {
        if (PointDto.class.equals(entityClass)) {
            return true;
        }

        return false;
    }

    @Override
    public Collection<Object> get(ServiceGetParams params) {

        switch (params.getType()) {
            case GET_ONE_BY_ELEMENT_ID:
                PointDto entity = pointRepository.findOne((long) params.getParameter(ServiceGetParams.NAME_ELEMENT_ID));
                return Arrays.asList(entity);
            case GET_ALL_BY_SCENE_ID:
                return (Collection) pointRepository.findAllBySceneDto((long) params.getParameter(ServiceGetParams.NAME_SCENE_ID));
            case GET_ALL:
                return (Collection) pointRepository.findAll();
            default:
                throw new IllegalArgumentException("parameters in point service is incorrect: " + params);
        }
    }

    @Override
    public PointDto create(BaseEntityDto entity) {
        throw new TcsServerRuntimeException("Not supported, please create from scene then save it");
    }

    @Override
    public PointDto update(BaseEntityDto entity) {
        PointDto point = (PointDto) Objects.requireNonNull(entity, "point entity is null");

        SceneDto scene = point.getSceneDto();

        final long id = point.getId();
        if (scene.getPointDtoById(id) == null) {
            throw new TcsServerRuntimeException("The point is not belonging to the scene " +
                    "or you can not create it by the method");
        }

        return pointRepository.save(point);
    }

    @Override
    public void delete(Long id) {
        throw new TcsServerRuntimeException("Not supported, please delete from scene then save it");
    }
}
