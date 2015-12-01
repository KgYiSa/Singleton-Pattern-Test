package com.mj.tcs.service.modelling;

import com.mj.tcs.api.v1.dto.PathDto;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.TcsServerRuntimeException;
import com.mj.tcs.repository.PathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@Component
public class PathDtoService implements IEntityDtoService {

    @Autowired
    PathRepository pathRepository;

    @Override
    public boolean canSupportEntityClass(Class entityClass) {
        if (PathDto.class.equals(entityClass)) {
            return true;
        }

        return false;
    }

    @Override
    public Collection<Object> get(ServiceGetParams params) {

        switch (params.getType()) {
            case GET_ONE_BY_ELEMENT_ID:
                PathDto entity = pathRepository.findOne((long) params.getParameter(ServiceGetParams.NAME_ELEMENT_ID));
                return Arrays.asList(entity);
            case GET_ALL_BY_SCENE_ID:
                return (Collection) pathRepository.findAllBySceneDto((long) params.getParameter(ServiceGetParams.NAME_SCENE_ID));
            case GET_ALL:
                return (Collection) pathRepository.findAll();
            default:
                throw new TcsServerRuntimeException("parameters in path service is incorrect: " + params);
        }
    }

    @Override
    public PathDto create(BaseEntityDto entity) {
        if (entity instanceof PathDto) {
            entity = createPath((PathDto) entity);
            return (PathDto) entity;
        } else {
            throw new TcsServerRuntimeException("create path with different valueconverter type");
        }
    }

    @Override
    public PathDto update(BaseEntityDto entity) {
        if (entity instanceof PathDto) {
            entity = updatePath((PathDto) entity);
            return (PathDto) entity;
        } else {
            throw new TcsServerRuntimeException("update path with different valueconverter type");
        }
    }

    @Override
    public void delete(Long id) {
        throw new TcsServerRuntimeException("Not supported, please delete from scene then save it");
    }

    private PathDto createPath(PathDto entity) {
        throw new TcsServerRuntimeException("Not supported, please create from scene then save it");
    }

    private PathDto updatePath(PathDto entity) {
        PathDto path = (PathDto) Objects.requireNonNull(entity, "path entity is null");
        final long srcPointId = Objects.requireNonNull(path.getSourcePointDto(), "The path object belongs no source point!").getId();
        final long dstPointId = Objects.requireNonNull(path.getDestinationPointDto(), "The path object belongs no destination point!").getId();

        SceneDto scene = path.getSceneDto();

        // check these components belongs to the scene
        final long id = path.getId();
        if (scene.getPathDtoById(id) == null) {
            throw new TcsServerRuntimeException("The path is not belonging to the scene " +
                    "or you can not create it by the method");
        }
        if (scene.getPointDtoById(srcPointId) == null) {
            throw new TcsServerRuntimeException("The source point of the path is not belonging to the scene");
        }
        if (scene.getPointDtoById(dstPointId) == null) {
            throw new TcsServerRuntimeException("The destination point of the path is not belonging to the scene");
        }

        return pathRepository.save(path);
//        throw new TcsServerRuntimeException("Not supported, please create from scene then save it");
    }

    private void checkNullException(Object entry, String message) {
        if (entry == null) {
            throw new ObjectUnknownException(message);
        }
    }

}
