package com.mj.tcs.service;

import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.exception.TCSServerRuntimeException;
import com.mj.tcs.repository.SceneDtoRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@Component
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class SceneDtoService {
    private static final String SCENE_NAME_NEW_SUFFIX = "_1";

    @Autowired
    private SceneDtoRepository sceneDtoRepository;

//    @Autowired
//    private SceneExtDtoRepository sceneExtDtoRepository;

    public Iterable<SceneDto> findAll() {
        return sceneDtoRepository.findAll();
    }

    public SceneDto findOne(long id) {
        SceneDto sceneDto = sceneDtoRepository.findOne(id);

        if (sceneDto != null) {
            // TODO: FOR Lazy loading issues
            Hibernate.initialize(sceneDto.getBlockDtos());
            if (sceneDto.getBlockDtos() != null) {
                sceneDto.getBlockDtos().forEach(v -> Hibernate.initialize(v.getMembers()));
            }
            Hibernate.initialize(sceneDto.getLocationDtos());
            if (sceneDto.getLocationDtos() != null) {
                sceneDto.getLocationDtos().forEach(v -> Hibernate.initialize(v.getAttachedLinks()));
            }
            Hibernate.initialize(sceneDto.getLocationTypeDtos());
            Hibernate.initialize(sceneDto.getPathDtos());
            Hibernate.initialize(sceneDto.getPointDtos());
            if (sceneDto.getPointDtos() != null) {
                sceneDto.getPointDtos().forEach(v -> {
                    Hibernate.initialize(v.getAttachedLinks());
                    Hibernate.initialize(v.getIncomingPaths());
                    Hibernate.initialize(v.getOutgoingPaths());
                });
            }
            Hibernate.initialize(sceneDto.getStaticRouteDtos());
            if (sceneDto.getStaticRouteDtos() != null) {
                sceneDto.getStaticRouteDtos().forEach(v -> Hibernate.initialize(v.getHops()));
            }
            Hibernate.initialize(sceneDto.getVehicleDtos());
        }

        return sceneDto;
    }

    public SceneDto create(SceneDto dto) {
        Objects.requireNonNull(dto, "new scene object is null");

        // check if we already have one name of the new scene, otherwise throw an exception
        final Collection<SceneDto> sceneDtos = (Collection)findAll();
        if (sceneDtos != null && sceneDtos.stream().anyMatch(v -> Objects.equals(v.getName(), dto.getName()))) {
            throw new TCSServerRuntimeException("The name of the new scene should be unique!");
        }

        SceneDto answer = sceneDtoRepository.save(dto);

//        SceneExtDto extDto = new SceneExtDto();
//        extDto.setSceneDto(answer);
//
//        sceneExtDtoRepository.save(extDto);

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
