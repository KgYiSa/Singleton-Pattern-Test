package com.mj.tcs.service.modelling;

import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.TcsServerRuntimeException;
import com.mj.tcs.data.model.Path;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.model.Scene;
import com.mj.tcs.data.base.BaseEntity;
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
public class PathService implements IEntityService {

    @Autowired
    PathRepository pathRepository;

    @Override
    public boolean canSupportEntityClass(Class entityClass) {
        if (Path.class.equals(entityClass)) {
            return true;
        }

        return false;
    }

    @Override
    public Collection<Object> get(ServiceGetParams params) {

        switch (params.getType()) {
            case GET_ONE_BY_ELEMENT_ID:
                Path entity = pathRepository.findOne((long) params.getParameter(ServiceGetParams.NAME_ELEMENT_ID));
                return Arrays.asList(entity);
            case GET_ALL_BY_SCENE_ID:
                return (Collection) pathRepository.findAllByScene((long) params.getParameter(ServiceGetParams.NAME_SCENE_ID));
            case GET_ALL:
                return (Collection) pathRepository.findAll();
            default:
                throw new TcsServerRuntimeException("parameters in path service is incorrect: " + params);
        }
    }

    @Override
    public Path create(BaseEntity entity) {
        if (entity instanceof Path) {
            entity = createPath((Path) entity);
            return (Path) entity;
        } else {
            throw new TcsServerRuntimeException("create path with different valueconverter type");
        }
    }

    @Override
    public Path update(BaseEntity entity) {
        if (entity instanceof Path) {
            entity = updatePath((Path) entity);
            return (Path) entity;
        } else {
            throw new TcsServerRuntimeException("update path with different valueconverter type");
        }
    }

    @Override
    public void delete(Long id) {
        throw new TcsServerRuntimeException("Not supported, please delete from scene then save it");
    }

    private Path createPath(Path entity) {
        throw new TcsServerRuntimeException("Not supported, please create from scene then save it");
    }

    private Path updatePath(Path entity) {
        Path path = (Path) Objects.requireNonNull(entity, "path entity is null");
        Point srcPoint = Objects.requireNonNull(path.getSourcePoint(), "The path object belongs no source point!");
        Point dstPoint = Objects.requireNonNull(path.getDestinationPoint(), "The path object belongs no destination point!");

        Scene scene = path.getScene();

        // check these components belongs to the scene
        final long id = path.getId();
        if (!scene.getPathById(id).isPresent()) {
            throw new TcsServerRuntimeException("The path is not belonging to the scene " +
                    "or you can not create it by the method");
        }
        final long srcPointId = srcPoint.getId();
        if (!scene.getPointById(srcPointId).isPresent()) {
            throw new TcsServerRuntimeException("The source point of the path is not belonging to the scene");
        }
        final long dstPointId = dstPoint.getId();
        if (!scene.getPointById(dstPointId).isPresent()) {
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
