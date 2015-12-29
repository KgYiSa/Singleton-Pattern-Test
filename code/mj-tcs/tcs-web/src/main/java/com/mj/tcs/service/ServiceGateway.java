package com.mj.tcs.service;

import com.mj.tcs.LocalKernel;
import com.mj.tcs.api.v1.dto.*;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.converter.SceneDtoConverter;
import com.mj.tcs.data.base.TCSObjectReference;
import com.mj.tcs.data.model.Location;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.data.order.DriveOrder;
import com.mj.tcs.data.order.TransportOrder;
import com.mj.tcs.drivers.BasicCommunicationAdapter;
import com.mj.tcs.drivers.CommunicationAdapterFactory;
import com.mj.tcs.eventsystem.EventHub;
import com.mj.tcs.eventsystem.SynchronousEventHub;
import com.mj.tcs.exception.ObjectUnknownException;
import com.mj.tcs.exception.TCSServerRuntimeException;
import com.mj.tcs.kernel.Scene;
import com.mj.tcs.kernel.StandardKernel;
import com.mj.tcs.util.eventsystem.TCSEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Wang Zhen
 */
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
@Component
public class ServiceGateway {
    private final static SceneDtoConverter sceneDtoConverter = new SceneDtoConverter();

    // ID <--> running SceneDto
    private Map<Long, LocalKernel> kernelRuntimeMapping = new LinkedHashMap<>();

    @Autowired
    SceneDtoService sceneDtoService;

    //////////////////////// MODELLING ///////////////////////////////

    public <T extends BaseEntityDto> T getTCSObjectDto(long sceneId,
                                                       Class<T> clazz,
                                                       long id)
            throws ObjectUnknownException {
        SceneDto sceneDto;
        synchronized (sceneDtoService) {
            sceneDto = sceneDtoService.findOne(sceneId);
        }
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
        SceneDto sceneDto;
        synchronized (sceneDtoService) {
            sceneDto = sceneDtoService.findOne(sceneId);
        }
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
        SceneDto sceneDto;
        synchronized (sceneDtoService) {
            sceneDto = sceneDtoService.findOne(sceneId);
        }
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
        SceneDto sceneDto;
        synchronized (sceneDtoService) {
            sceneDto = sceneDtoService.findOne(sceneId);
        }
        return sceneDto;
    }

    public Collection<SceneDto> findAllScene() {
        synchronized (sceneDtoService) {
            return (Collection) sceneDtoService.findAll();
        }
    }

    public SceneDto createScene(SceneDto dto) {
        synchronized (sceneDtoService) {
            return sceneDtoService.create(dto);
        }
    }

    public SceneDto updateScene(SceneDto dto) {
        checkSceneDtoRunning(dto);

        synchronized (sceneDtoService) {
            return sceneDtoService.update(dto);
        }
    }

    public void deleteScene(long sceneId) {
        checkSceneDtoRunning(sceneId);

        synchronized (sceneDtoService) {
            sceneDtoService.delete(sceneId);
        }
    }

    ////////////////////////////// OPERATTING /////////////////////////////////
    public boolean loadSceneDto(long sceneId) {
        SceneDto sceneDto;
        synchronized (sceneDtoService) {
            sceneDto = sceneDtoService.findOne(sceneId);
        }
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
        Scene scene = sceneDtoConverter.convertToEntity(sceneDto);
        EventHub<TCSEvent> eventHub = new SynchronousEventHub<>();
        KernelEventHandler eventHandler = new KernelEventHandler();
        eventHub.addEventListener(eventHandler, eventHandler);
        LocalKernel kernel = new StandardKernel(eventHub, scene);
        try {
            kernel.initialize();
            final Vehicle v = (Vehicle) kernel.getTCSObjects(Vehicle.class).toArray()[0];
            final Point p = ((Point) kernel.getTCSObjects(Point.class).toArray()[0]);
            kernel.setVehiclePosition(v.getReference(),
                    p.getReference());
            // adapter
            CommunicationAdapterFactory adapterFactory = kernel.getCommAdapterRegistry().findFactoriesFor(v).get(0);
            BasicCommunicationAdapter adapter = adapterFactory.getAdapterFor(v);
            adapter.enable();
            kernel.getVehicleManagerPool().getVehicleManager(v.getUUID(), adapter);
            // transport order
            List<DriveOrder.Destination> destinations = new ArrayList<>();
            TCSObjectReference<Location> locRef = TCSObjectReference.getDummyReference(Point.class, p.getUUID());
            destinations.add(new DriveOrder.Destination(locRef, "MOVE"));
            TransportOrder torder = kernel.createTransportOrder(destinations);
            kernel.activateTransportOrder(torder.getReference());
            // dispatch vehicle
            kernel.dispatchVehicle(v.getReference(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        kernelRuntimeMapping.put(idKey, kernel);

        return true;
    }

    public void unloadSceneDto(long sceneId) {
        SceneDto sceneDto;
        synchronized (sceneDtoService) {
            sceneDto = sceneDtoService.findOne(sceneId);
        }
        updateScene(sceneDto);
    }

    public void unloadSceneDto(SceneDto sceneDto) {
        Objects.requireNonNull(sceneDto);
        Long idKey = Objects.requireNonNull(sceneDto.getId());

        if (isSceneDtoRunning(sceneDto)) {
            // Stop kernel for the sceneDto
            // TODO:

            // remove it
            LocalKernel kernel = kernelRuntimeMapping.remove(idKey);
            kernel.terminate();
        }
    }

    public Set<SceneDto> getRunningSceneDtos() {
        return kernelRuntimeMapping.keySet().stream()
                .map(v -> getSceneDto(v)).collect(Collectors.toSet());
    }

    public boolean isSceneDtoRunning(SceneDto sceneDto) {
        Objects.requireNonNull(sceneDto);
        Long idKey = Objects.requireNonNull(sceneDto.getId());

        return kernelRuntimeMapping.containsKey(idKey);
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
            throw new TCSServerRuntimeException("The scene by id [" + sceneDto.getId() + "] is running!");
        }
    }
}
