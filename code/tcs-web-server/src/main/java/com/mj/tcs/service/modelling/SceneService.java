package com.mj.tcs.service.modelling;

import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.data.model.Scene;
import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.repository.SceneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wang Zhen
 */
@Component
public class SceneService implements IEntityService {
    private static final String SCENE_NAME_NEW_SUFFIX = "_1";

    @Autowired
    private SceneRepository sceneRepository;

    @Override
    public boolean canSupportEntityClass(Class entityClass) {
        if (Scene.class.equals(entityClass)) {
            return true;
        }

        return false;
    }

    @Override
    public Collection<Object> get(ServiceGetParams params) {

        switch (params.getType()) {
            case GET_ONE_BY_ELEMENT_ID:
            case GET_ALL_BY_SCENE_ID:
                Scene entity = sceneRepository.findOne((long) params.getParameter(ServiceGetParams.NAME_SCENE_ID));
                return Arrays.asList(entity);
            case GET_ALL:
                return (Collection) sceneRepository.findAll();
            default:
                throw new IllegalArgumentException("parameters in scene service is incorrect: " + params);
        }
    }

    @Override
    public Scene create(BaseEntity entity) {
        if (entity instanceof Scene) {
            entity = createScene((Scene) entity);
            return (Scene) entity;
        } else {
            throw new IllegalArgumentException("create scene with different valueconverter type");
        }
    }

    @Override
    public Scene update(BaseEntity entity) {
        if (entity instanceof Scene) {
            entity = createScene((Scene) entity);
            return (Scene) entity;
        } else {
            throw new IllegalArgumentException("update scene with different valueconverter type");
        }
    }

    @Override
    public void delete(Long id) {
        sceneRepository.delete(id);
    }

    // CREATING, UPDATE, PATCH, DELETE
    private Scene createScene(Scene entity) {
        checkNullException(entity, "new scene object is null");

        // check if we already have one name of the new scene, otherwise choose another one
            String newSceneName = entity.getName();
            List<String> sceneNames = new ArrayList<>();
            if (getAllScenes() != null) {
                sceneNames.addAll(getAllScenes().stream().map(Scene::getName).collect(Collectors.toList()));
                while (sceneNames.contains(newSceneName)) {
                    newSceneName += SCENE_NAME_NEW_SUFFIX;
                }
                entity.setName(newSceneName);
            }

        entity = sceneRepository.save(entity);
        return entity;
    }

    private Scene updateScene(Scene entity) {
        checkNullException(entity, "updated scene object is null");

        Scene oldEntity = sceneRepository.findOne(entity.getId());

        // TODO:
//        entity.setCreatedAt(oldEntity.getCreatedAt());
//        entity.setCreatedBy(oldEntity.getCreatedBy());

        entity = sceneRepository.save(entity);

        return entity;
    }

    // OPERATIONS

    private Collection<Scene> getAllScenes() {
        Collection<Scene> scenes = new ArrayList<>();

        for (Scene scene : sceneRepository.findAll()) {
            scenes.add(scene);
        }

        return scenes;
    }

    // PRIVATE METHODS

    private void checkNullException(Object entry, String message) {
        if (entry == null) {
            throw new ObjectUnknownException(message);
        }
    }
}
