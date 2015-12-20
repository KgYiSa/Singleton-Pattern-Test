package com.mj.tcs.service;

import com.mj.tcs.api.v1.dto.*;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.TcsServerRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Wang Zhen
 */
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
@Component
public class ServiceGateway {
    // ID <--> running SceneDto
    private Map<Long, SceneDto> sceneDtoRuntimeMapping = new LinkedHashMap<>();

    @Autowired
    SceneDtoService sceneDtoService;

    //////////////////////// MODELLING ///////////////////////////////

    public <T extends BaseEntityDto> T getTCSObjectDto(long sceneId,
                                                             Class<T> clazz,
                                                             long id)
            throws ObjectUnknownException {
        SceneDto sceneDto = sceneDtoService.findOne(sceneId);
        Objects.requireNonNull(sceneDto);
        Objects.requireNonNull(clazz, "clazz is null");

        if (BlockDto.class.equals(clazz)) {
            return clazz.cast(sceneDto.getBlockDtoById(id));
        }
        /*else if (Group.class.equals(clazz)) {
            sceneDto.removeGroup(((Group) object).getReference());
        }*/
        else if (LocationDto.class.equals(clazz)) {
            return clazz.cast(sceneDto.getLocationDtoById(id));
        }
        else if (LocationTypeDto.class.equals(clazz)) {
            return clazz.cast(sceneDto.getLocationTypeDtoById(id));
        }
        else if (PathDto.class.equals(clazz)) {
            return clazz.cast(sceneDto.getPathDtoById(id));
        }
        else if (PointDto.class.equals(clazz)) {
            return clazz.cast(sceneDto.getPointDtoById(id));
        }
        else if (StaticRouteDto.class.equals(clazz)) {
            return clazz.cast(sceneDto.getStaticRouteDtoById(id));
        }
        else if (VehicleDto.class.equals(clazz)) {
            return clazz.cast(sceneDto.getVehicleDtoById(id));
        }

        throw new ObjectUnknownException(clazz);
    }

    public <T extends BaseEntityDto> T getTCSObjectDto(long sceneId,
                                                             Class<T> clazz,
                                                             String name)
            throws ObjectUnknownException {
        SceneDto sceneDto = sceneDtoService.findOne(sceneId);
        Objects.requireNonNull(sceneDto);
        Objects.requireNonNull(clazz, "clazz is null");

        // TODO
//        if (BlockDto.class.equals(clazz)) {
//            return clazz.cast(sceneDto.getBlockDtoByName(id));
//        }
//        /*else if (Group.class.equals(clazz)) {
//            sceneDto.removeGroup(((Group) object).getReference());
//        }*/
//        else if (LocationDto.class.equals(clazz)) {
//            return clazz.cast(sceneDto.getLocationDtoById(id));
//        }
//        else if (LocationTypeDto.class.equals(clazz)) {
//            return clazz.cast(sceneDto.getLocationTypeDtoById(id));
//        }
//        else if (PathDto.class.equals(clazz)) {
//            return clazz.cast(sceneDto.getPathDtoById(id));
//        }
//        else if (PointDto.class.equals(clazz)) {
//            return clazz.cast(sceneDto.getPointDtoById(id));
//        }
//        else if (StaticRouteDto.class.equals(clazz)) {
//            return clazz.cast(sceneDto.getStaticRouteDtoById(id));
//        }
//        else if (VehicleDto.class.equals(clazz)) {
//            return clazz.cast(sceneDto.getVehicleDtoById(id));
//        }

        throw new ObjectUnknownException(clazz);
    }


    public <T extends BaseEntityDto> Set<T> getTcsObjectDtos(long sceneId,
                                                                    Class<T> clazz)
            throws ObjectUnknownException {
        SceneDto sceneDto = sceneDtoService.findOne(sceneId);
        Objects.requireNonNull(sceneDto);
        Objects.requireNonNull(clazz, "clazz is null");

        Set<T> copiers = new HashSet<>();

        if (BlockDto.class.equals(clazz)) {
            if (sceneDto.getBlockDtos() != null) {
                sceneDto.getBlockDtos().forEach(v -> copiers.add(clazz.cast(v)));
            }
            return copiers;
        }
    /*else if (Group.class.equals(clazz)) {
        scene.removeGroup(((Group) object).getReference());
    }*/
        else if (LocationDto.class.equals(clazz)) {
            if (sceneDto.getLocationDtos() != null) {
                sceneDto.getLocationDtos().forEach(v -> copiers.add(clazz.cast(v)));
            }
            return copiers;
        }
        else if (LocationTypeDto.class.equals(clazz)) {
            if (sceneDto.getLocationTypeDtos() != null) {
                sceneDto.getLocationTypeDtos().forEach(v -> copiers.add(clazz.cast(v)));
            }
            return copiers;
        }
        else if (PathDto.class.equals(clazz)) {
            if (sceneDto.getPathDtos() != null) {
                sceneDto.getPathDtos().forEach(v -> copiers.add(clazz.cast(v)));
            }
            return copiers;
        }
        else if (PointDto.class.equals(clazz)) {
            if (sceneDto.getPointDtos() != null) {
                sceneDto.getPointDtos().forEach(v -> copiers.add(clazz.cast(v)));
            }
            return copiers;
        }
        else if (StaticRouteDto.class.equals(clazz)) {
            if (sceneDto.getStaticRouteDtos() != null) {
                sceneDto.getStaticRouteDtos().forEach(v -> copiers.add(clazz.cast(v)));
            }
            return copiers;
        }
        else if (VehicleDto.class.equals(clazz)) {
            if (sceneDto.getVehicleDtos() != null) {
                sceneDto.getVehicleDtos().forEach(v -> copiers.add(clazz.cast(v)));
            }
            return copiers;
        }

        throw new ObjectUnknownException(clazz);
    }

    public <T extends BaseEntityDto> Set<T> getTCSObjectDtos(long sceneId,
                                                                    Class<T> clazz,
                                                                    Pattern regexp)
            throws ObjectUnknownException {
        // TODO:
        throw new ObjectUnknownException(clazz);
    }

    //SCENES
    public SceneDto getSceneDto(long sceneId) {
        return sceneDtoService.findOne(sceneId);
    }

    public Collection<SceneDto> findAllScene() {
        return (Collection)sceneDtoService.findAll();
    }

    public SceneDto createScene(SceneDto dto) {
        return sceneDtoService.create(dto);
    }

    public SceneDto updateScene(SceneDto dto) {
        checkSceneDtoRunning(dto);

        return sceneDtoService.update(dto);
    }

    public void deleteScene(long sceneId) {
        checkSceneDtoRunning(sceneId);

        sceneDtoService.delete(sceneId);
    }

    ////////////////////////////// OPERATTING /////////////////////////////////
    public boolean loadSceneDto(long sceneId) {
        SceneDto sceneDto = sceneDtoService.findOne(sceneId);
        return loadSceneDto(sceneDto);
    }

    public boolean loadSceneDto(SceneDto sceneDto) {
        Objects.requireNonNull(sceneDto);
        Long idKey = Objects.requireNonNull(sceneDto.getId());

        if (isSceneDtoRunning(sceneDto)) {
            return false; // Already running
        }

        // load kernel for the sceneDto
        // TODO:

        sceneDtoRuntimeMapping.put(idKey, sceneDto);
        return true;
    }

    public void unloadSceneDto(long sceneId) {
        SceneDto sceneDto = sceneDtoService.findOne(sceneId);
        updateScene(sceneDto);
    }

    public void unloadSceneDto(SceneDto sceneDto) {
        Objects.requireNonNull(sceneDto);
        Long idKey = Objects.requireNonNull(sceneDto.getId());

        if (isSceneDtoRunning(sceneDto)) {
            // Stop kernel for the sceneDto
            // TODO:

            // remove it
            sceneDtoRuntimeMapping.remove(idKey);
        }
    }

    public boolean isSceneDtoRunning(SceneDto sceneDto) {
        Objects.requireNonNull(sceneDto);
        Long idKey = Objects.requireNonNull(sceneDto.getId());

        return sceneDtoRuntimeMapping.containsKey(idKey);
    }

    public boolean isSceneDtoRunning(long sceneId) {
        return isSceneDtoRunning(getSceneDto(sceneId));
    }

    private void checkSceneDtoRunning(long sceneId) {
        checkSceneDtoRunning(getSceneDto(sceneId));
    }

    private void checkSceneDtoRunning(SceneDto sceneDto) {
        Objects.requireNonNull(sceneDto);

        if (isSceneDtoRunning(sceneDto)) {
            Objects.requireNonNull(sceneDto.getId());
            throw new TcsServerRuntimeException("The scene by id [" + sceneDto.getId() + "] is running!");
        }
    }
}
