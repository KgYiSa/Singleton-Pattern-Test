package com.mj.tcs.service;

import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.connectity.SceneExtDto;
import com.mj.tcs.exception.TcsServerRuntimeException;
import com.mj.tcs.repository.SceneDtoRepository;
import com.mj.tcs.repository.SceneExtDtoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@Component
public class SceneDtoService {
    private static final String SCENE_NAME_NEW_SUFFIX = "_1";

    @Autowired
    private SceneDtoRepository sceneDtoRepository;

    @Autowired
    private SceneExtDtoRepository sceneExtDtoRepository;

    public Iterable<SceneDto> findAll() {
        return sceneDtoRepository.findAll();
    }

    public SceneDto findOne(long id) {
        return sceneDtoRepository.findOne(id);
    }

    public SceneDto create(SceneDto dto) {
        Objects.requireNonNull(dto, "new scene object is null");

        // check if we already have one name of the new scene, otherwise throw an exception
        final Collection<SceneDto> sceneDtos = (Collection)findAll();
        if (sceneDtos != null && sceneDtos.stream().anyMatch(v -> Objects.equals(v.getName(), dto.getName()))) {
            throw new TcsServerRuntimeException("The name of the new scene should be unique!");
        }

        SceneDto answer = sceneDtoRepository.save(dto);

        SceneExtDto extDto = new SceneExtDto();
        extDto.setSceneDto(answer);

        sceneExtDtoRepository.save(extDto);

        return answer;
    }

    public SceneDto update(SceneDto dto) {
        Objects.requireNonNull(dto, "updated scene object is null");

        SceneDto oldDto = sceneDtoRepository.findOne(dto.getId());

        // TODO: merge && update creation & update time
//        dto.setCreatedAt(oldEntity.getCreatedAt());
//        dto.setCreatedBy(oldEntity.getCreatedBy());

        return sceneDtoRepository.save(dto);
    }

    public void delete(long id) {
        sceneDtoRepository.delete(id);
    }
}
