package com.mj.tcs.service.modelling;

import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.repository.SceneDtoRepository;
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
public class SceneDtoService implements IEntityDtoService {
    private static final String SCENE_NAME_NEW_SUFFIX = "_1";

    @Autowired
    private SceneDtoRepository sceneDtoRepository;

    @Override
    public boolean canSupportEntityClass(Class entityClass) {
        if (SceneDto.class.equals(entityClass)) {
            return true;
        }

        return false;
    }

    @Override
    public Collection<Object> get(ServiceGetParams params) {

        switch (params.getType()) {
            case GET_ONE_BY_ELEMENT_ID:
            case GET_ALL_BY_SCENE_ID:
                SceneDto entity = sceneDtoRepository.findOne((long) params.getParameter(ServiceGetParams.NAME_SCENE_ID));
                return Arrays.asList(entity);
            case GET_ALL:
                return (Collection) sceneDtoRepository.findAll();
            default:
                throw new IllegalArgumentException("parameters in scene service is incorrect: " + params);
        }
    }

    @Override
    public SceneDto create(BaseEntityDto entity) {
        if (entity instanceof SceneDto) {
            entity = createScene((SceneDto) entity);
            return (SceneDto) entity;
        } else {
            throw new IllegalArgumentException("create scene with different valueconverter type");
        }
    }

    @Override
    public SceneDto update(BaseEntityDto entity) {
        if (entity instanceof SceneDto) {
            entity = createScene((SceneDto) entity);
            return (SceneDto) entity;
        } else {
            throw new IllegalArgumentException("update scene with different valueconverter type");
        }
    }

    @Override
    public void delete(Long id) {
        sceneDtoRepository.delete(id);
    }

    // CREATING, UPDATE, PATCH, DELETE
    private SceneDto createScene(SceneDto entity) {
        checkNullException(entity, "new scene object is null");

        // check if we already have one name of the new scene, otherwise choose another one
            String newSceneName = entity.getName();
            List<String> sceneNames = new ArrayList<>();
            if (getAllScenes() != null) {
                sceneNames.addAll(getAllScenes().stream().map(SceneDto::getName).collect(Collectors.toList()));
                while (sceneNames.contains(newSceneName)) {
                    newSceneName += SCENE_NAME_NEW_SUFFIX;
                }
                entity.setName(newSceneName);
            }

        entity = sceneDtoRepository.save(entity);
        return entity;
    }

    private SceneDto updateScene(SceneDto entity) {
        checkNullException(entity, "updated scene object is null");

        SceneDto oldEntity = sceneDtoRepository.findOne(entity.getId());

        // TODO:
//        entity.setCreatedAt(oldEntity.getCreatedAt());
//        entity.setCreatedBy(oldEntity.getCreatedBy());

        entity = sceneDtoRepository.save(entity);

        return entity;
    }

    // OPERATIONS

    private Collection<SceneDto> getAllScenes() {
        Collection<SceneDto> scenes = new ArrayList<>();

        for (SceneDto scene : sceneDtoRepository.findAll()) {
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
