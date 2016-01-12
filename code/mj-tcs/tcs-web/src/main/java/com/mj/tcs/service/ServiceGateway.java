package com.mj.tcs.service;

import com.mj.tcs.LocalKernel;
import com.mj.tcs.api.v1.dto.*;
import com.mj.tcs.api.v1.dto.base.BaseEntityDto;
import com.mj.tcs.api.v1.dto.converter.SceneDtoConverter;
import com.mj.tcs.api.v1.sock.SceneStatusSockController;
import com.mj.tcs.api.v1.sock.TransportOrderStatusSockController;
import com.mj.tcs.api.v1.sock.VehicleStatusSockController;
import com.mj.tcs.config.AppContext;
import com.mj.tcs.data.base.TCSObjectReference;
import com.mj.tcs.data.model.Location;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.data.order.DriveOrder;
import com.mj.tcs.data.order.TransportOrder;
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

import javax.annotation.PreDestroy;
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

    @Autowired
    TransportOrderDtoService orderDtoService;

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
        assertSceneDtoStatus(dto, true);

        synchronized (sceneDtoService) {
            return sceneDtoService.update(dto);
        }
    }

    public void deleteScene(long sceneId) {
        assertSceneDtoStatus(sceneId, true);

        synchronized (sceneDtoService) {
            sceneDtoService.delete(sceneId);
        }
    }

    ////////////////////////////// OPERATTING /////////////////////////////////
    @PreDestroy
    public void shutdown() {
        kernelRuntimeMapping.keySet().forEach(this::unloadSceneDto);
    }

    public LocalKernel getKernel(long sceneId) {
        return kernelRuntimeMapping.get(sceneId);
    }

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
        final KernelEventHandler eventHandler = new KernelEventHandler(idKey);

        // TODO: Register Controller as listeners
        Map<String, SceneStatusSockController> controllerMap1 = AppContext.getContext().getBeansOfType(SceneStatusSockController.class);
        controllerMap1.values().forEach(eventHandler::register);
        Map<String, TransportOrderStatusSockController> controllerMap2 = AppContext.getContext().getBeansOfType(TransportOrderStatusSockController.class);
        controllerMap2.values().forEach(eventHandler::register);
        Map<String, VehicleStatusSockController> controllerMap3 = AppContext.getContext().getBeansOfType(VehicleStatusSockController.class);
        controllerMap3.values().forEach(eventHandler::register);

        eventHub.addEventListener(eventHandler, eventHandler);
        LocalKernel kernel = new StandardKernel(eventHub, scene);
        try {
            kernel.initialize();
            // TODO: For testing only!!!
//            // vehicle & setVehiclePosition
//            final Vehicle vehicle = (Vehicle) kernel.getTCSObjects(Vehicle.class).toArray()[0];
//            final Point point = ((Point) kernel.getTCSObjects(Point.class).toArray()[0]);
//
//            // adapter
//            CommunicationAdapterFactory adapterFactory = kernel.getCommAdapterRegistry().findFactoriesFor(vehicle).get(0);
//            kernel.attachAdapterToVehicle(vehicle.getReference(), adapterFactory);
//
//            BasicCommunicationAdapter adapter = kernel.findAdapterFor(vehicle.getReference());
//            adapter.enable();// enable
//            if (adapter instanceof SimCommunicationAdapter) {
//                ((SimCommunicationAdapter) adapter).initVehiclePosition(point.getUUID());
//            }
//
//            // transport order
//            List<DriveOrder.Destination> destinations = new ArrayList<>();
//            TCSObjectReference<Location> locRef = TCSObjectReference.getDummyReference(Point.class, point.getUUID());
//            destinations.add(new DriveOrder.Destination(locRef, "MOVE"));
//            TransportOrder torder = kernel.createTransportOrder(destinations);
//            kernel.activateTransportOrder(torder.getReference());
//
//            // dispatch vehicle
//            kernel.dispatchVehicle(vehicle.getReference(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        kernelRuntimeMapping.put(idKey, kernel);

        return true;
    }

    public void unloadSceneDto(SceneDto sceneDto) {
        Objects.requireNonNull(sceneDto);
        Long idKey = Objects.requireNonNull(sceneDto.getId());

        unloadSceneDto(idKey);
//        if (isSceneDtoRunning(sceneDto)) {
//            // Stop kernel for the sceneDto
//
//
//            // remove it & terminate kernel
//            LocalKernel kernel = kernelRuntimeMapping.remove(idKey);
//            kernel.terminate();
//        }
    }

    public void unloadSceneDto(long sceneId) {
//        SceneDto sceneDto;
//        synchronized (sceneDtoService) {
//            sceneDto = sceneDtoService.findOne(sceneId);
//        }
//        updateScene(sceneDto);
        if (isSceneDtoRunning(sceneId)) {
            // Stop kernel for the sceneDto


            // remove it & terminate kernel
            LocalKernel kernel = kernelRuntimeMapping.remove(sceneId);
            kernel.terminate();
            kernel.waitForTermination();
        }
    }

    public Set<SceneDto> getRunningSceneDtos() {
        return kernelRuntimeMapping.keySet().stream()
                .map(v -> getSceneDto(v)).collect(Collectors.toSet());
    }

    public boolean isSceneDtoRunning(SceneDto sceneDto) {
        Objects.requireNonNull(sceneDto);
        Long idKey = Objects.requireNonNull(sceneDto.getId());

        return isSceneDtoRunning(idKey);
    }

    public boolean isSceneDtoRunning(long sceneId) {
//        return isSceneDtoRunning(getSceneDto(sceneId));
        return kernelRuntimeMapping.containsKey(sceneId);
    }

    /////////////// Transport Order //////////////
    public TransportOrderDto createTransportOrder(long sceneId, TransportOrderDto newOrderDto) throws Exception {
        assertSceneDtoStatus(sceneId, false);
        Objects.requireNonNull(newOrderDto);

        final LocalKernel kernel = getKernel(sceneId);
        final TransportOrder order;
        // Convert from DTO to Entity
        {
            // Destinations
            List<DriveOrder.Destination> realDests = new LinkedList<>();
            for (DestinationDto destDto : newOrderDto.getDestinations()) {
                final String locUUID = destDto.getLocationUUID();
                final String operation = destDto.getOperation();
                if (destDto.isDummy()) {
                    // TODO: Refine const "MOVE"
                    realDests.add(new DriveOrder.Destination(TCSObjectReference.getDummyReference(Point.class, locUUID), "MOVE"));
                } else {
                    // Get Location from kernel
                    final Location location = Objects.requireNonNull(kernel.getTCSObject(Location.class, locUUID));
                    realDests.add(new DriveOrder.Destination(location.getReference(), operation));
                }
            }

            // New Transport order
            // TODO: Use kernel's internal events
            order = kernel.createTransportOrder(newOrderDto.getUUID(), newOrderDto.getName(), realDests);
            // TODO: Polish Prefix
//            order = new TransportOrder("TO-"+UUID.randomUUID(), "TO-"+new UniqueTimestampGenerator().getNextTimestampInStringFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SS")), realDests);

            // Set the transport order's deadline, if any.
            if (newOrderDto.getDeadline() != null) {
                kernel.setTransportOrderDeadline(order.getReference(), newOrderDto.getDeadline());
            }

            // Sets the intended vehicle, if any.
            String vehicleUUID = newOrderDto.getIntendedVehicleUUID();
            if (vehicleUUID != null && !vehicleUUID.isEmpty()) {
                Vehicle vehicle = kernel.getTCSObject(Vehicle.class, vehicleUUID);
                if (vehicle == null) {
                    // TODO: Set state of created order to FAILED?
                    throw new com.mj.tcs.data.ObjectUnknownException("Unknown vehicle: " + vehicleUUID);
                }
//                order.setIntendedVehicle(vehicle.getReference());
                kernel.setTransportOrderIntendedVehicle(order.getReference(), vehicle.getReference());
            }

            // Set the transport order's dependencies, if any.
            if (newOrderDto.getDeps() != null) {
                List<String> toRemove = new ArrayList<>();
                newOrderDto.getDeps().forEach(v -> {
                    final TransportOrder curDep = kernel.getTCSObject(TransportOrder.class,
                            v);
                    // If curDep is null, remove it - it might have been processed and
                    // removed already.
                    if (curDep != null) {
                        kernel.addTransportOrderDependency(order.getReference(),
                                curDep.getReference());
                    } else {
                        toRemove.add(v);
                    }
                });
                newOrderDto.getDeps().removeAll(toRemove);
            }
        }

        // Save to Database.
        try {
            newOrderDto = orderDtoService.create(newOrderDto);
        } catch (Exception e) {
            kernel.removeTransportOrder(order.getReference());
            throw new Exception(e);
        }

        // Activate the new transport order.
        kernel.activateTransportOrder(order.getReference());

        return newOrderDto;
    }

    public void withdrawTransportOrder(long sceneId, TransportWithdrawDto withdrawDto) throws Exception {
        assertSceneDtoStatus(sceneId, false);
        Objects.requireNonNull(withdrawDto);

        final LocalKernel kernel = getKernel(sceneId);

        TransportOrder transportOrder = Objects.requireNonNull(kernel.getTCSObject(TransportOrder.class, withdrawDto.getUUID()));
        boolean force = withdrawDto.isForce();
        boolean disableVehicle = withdrawDto.isDisableVehicle();
        kernel.withdrawTransportOrder(transportOrder.getReference(), disableVehicle);
        if (force) {
            kernel.withdrawTransportOrder(transportOrder.getReference(), disableVehicle);
        }

        // TODO: Save to Database.
    }

    /////////////// PRIVATE METHODS //////////////
    private void assertSceneDtoStatus(SceneDto sceneDto, boolean assertRunning) throws TCSServerRuntimeException {
        Objects.requireNonNull(sceneDto);
        Long idKey = Objects.requireNonNull(sceneDto.getId());

        assertSceneDtoStatus(idKey, assertRunning);
    }

    private void assertSceneDtoStatus(long sceneId, boolean assertRunning) throws TCSServerRuntimeException {
        boolean isRunning = isSceneDtoRunning(sceneId);
        if (!(assertRunning ^ isRunning)) {
            throw new TCSServerRuntimeException("The scene by id [" + sceneId + "] is " + (assertRunning ? "running":"stopped"));
        }
    }
}
