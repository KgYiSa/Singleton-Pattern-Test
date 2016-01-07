package com.mj.tcs.api.v1.sock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.tcs.LocalKernel;
import com.mj.tcs.api.v1.dto.TransportWithdrawDto;
import com.mj.tcs.api.v1.dto.VehicleAdapterAttacherDto;
import com.mj.tcs.api.v1.dto.communication.TCSRequestEntity;
import com.mj.tcs.api.v1.dto.communication.TCSResponseEntity;
import com.mj.tcs.api.v1.web.ServiceController;
import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.drivers.BasicCommunicationAdapter;
import com.mj.tcs.drivers.CommunicationAdapterFactory;
import com.mj.tcs.drivers.CommunicationAdapterRegistry;
import com.mj.tcs.drivers.SimCommunicationAdapter;
import com.mj.tcs.service.ServiceGateway;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Wang Zhen
 */
@Controller
public class VehicleCommandSockController extends ServiceController{
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @MessageMapping("/topic/actions/scenes/{sceneId}/vehicles/request")
    @SendToUser("/topic/actions/response")
    public TCSResponseEntity<?> executeAction(@DestinationVariable Long sceneId,
                                              TCSRequestEntity request,
                                              SimpMessageHeaderAccessor ha) {
        Objects.requireNonNull(sceneId);
        Objects.requireNonNull(request);
        TCSResponseEntity<?> responseEntity = null;

        TCSRequestEntity.Action actionCode = request.getActionCode();
        if (actionCode == null) {// Check actionCode
            return new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR,
                    "The action code is null.");
        }

        // check the scene is running.
        final ServiceGateway serviceGateway = getService();
        final LocalKernel kernel = serviceGateway.getKernel(sceneId);
        if (kernel == null || !serviceGateway.isSceneDtoRunning(sceneId)) {
            return new TCSResponseEntity<>(TCSResponseEntity.Status.WARNING, null,
                    "The scene of id [" + sceneId +"] is NOT running!");
        }

        try {
            switch (actionCode) {
                case VEHICLE_PROFILE:
                    responseEntity = getAllVehicleProfile(kernel, sceneId, null);
                    break;
                case VEHICLE_ADAPTER_ATTACH:
                    responseEntity = attachAdapterToVehicle(kernel, sceneId, request.getBody());
                    break;
                case VEHICLE_DISPATCH:
                    responseEntity = dispatchVehicle(kernel, sceneId, request.getBody());
                    break;
                case VEHICLE_STOP_TO:
                    responseEntity = withdrawTObyVehicle(kernel, sceneId, request.getBody());
                    break;
                case VEHICLE_PARK:
                    responseEntity = parkVehicle(kernel, sceneId, request.getBody());
                    break;
                default:
                    throw new IllegalArgumentException("The action code [" + actionCode + "] is not recognized.");
            }
        } catch (Exception e) {
            responseEntity = new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR,null, e.getMessage());
        }

        responseEntity.setResponseUUID(request.getRequestUUID());
        return responseEntity;
    }

    private  TCSResponseEntity<?> getAllVehicleProfile(final LocalKernel kernel, long sceneId, Object jsonBody) throws Exception {
        List<Map<String, String>> vehiclesProfile = new ArrayList<>();
        Set<Vehicle> vehicles = Objects.requireNonNull(kernel.getTCSObjects(Vehicle.class));
        final CommunicationAdapterRegistry communicationAdapterRegistry = Objects.requireNonNull(kernel.getCommAdapterRegistry());

        for (final Vehicle vehicle : vehicles) {
            Map<String, String> item = new LinkedHashMap<>();
            item.put("uuid", vehicle.getUUID());
            item.put("name", vehicle.getName());
            item.put("position_uuid", (vehicle.getCurrentPosition() == null ? "":vehicle.getCurrentPosition().getUUID()));
            item.put("state", vehicle.getState().name());
            BasicCommunicationAdapter adapter = kernel.findAdapterFor(vehicle.getReference());
            if (adapter == null) {
                item.put("adapter", "");
                item.put("enable", String.valueOf(false));
            } else {
                item.put("adapter", adapter.getClass().getSimpleName());
                item.put("enable", String.valueOf(adapter.isEnabled()));
            }
            item.put("adapter_state", vehicle.getAdapterState().name());
            item.put("processing_state", vehicle.getProcState().name());
            List<CommunicationAdapterFactory> factories = communicationAdapterRegistry.findFactoriesFor(vehicle);
            if (factories == null) {
                item.put("adapters", "");
            } else {
                item.put("adapters", objectMapper.writeValueAsString(factories.stream()
                        .filter(v -> v.providesAdapterFor(vehicle))
                        .map(v -> v.getAdapterFor(vehicle).getClass().getSimpleName())
                        .collect(Collectors.toList())));
            }
            vehiclesProfile.add(item);
        }

        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, vehiclesProfile);
    }

    private  TCSResponseEntity<?> attachAdapterToVehicle(final LocalKernel kernel, long sceneId, Object jsonBody) throws Exception {
        if (jsonBody == null || jsonBody.toString().isEmpty()) {
            throw new IllegalArgumentException("The vehicle can not be attached with EMPTY content");
        }
        List<Map<String, String>> updatedVehiclesProfile = new ArrayList<>();

        List<VehicleAdapterAttacherDto> adapterAttacherSetDto = new ArrayList<>();
        for (Object element : (List)jsonBody) {
            String json = objectMapper.writeValueAsString(element);
            adapterAttacherSetDto.add(objectMapper.readValue(json, VehicleAdapterAttacherDto.class));
        }
        final List<String> vehicleUUIDs = adapterAttacherSetDto.stream().map(v -> v.getAdapterName())
                .collect(Collectors.toList());

        Set<Vehicle> vehicles = Objects.requireNonNull(kernel.getTCSObjects(Vehicle.class));
        final CommunicationAdapterRegistry communicationAdapterRegistry = Objects.requireNonNull(kernel.getCommAdapterRegistry());

        for (Vehicle vehicle : vehicles) {
            Optional<VehicleAdapterAttacherDto> adapterAttacherDtoOptional =
                    adapterAttacherSetDto.stream().filter(v -> vehicle.getUUID().equals(v.getVehicleUUID()))
                    .findAny();
            if (!adapterAttacherDtoOptional.isPresent()) {
                continue;
            }

            VehicleAdapterAttacherDto adapterAttacherDto = adapterAttacherDtoOptional.get();

            // attach communication factory
            Optional<CommunicationAdapterFactory> factoryOptional = communicationAdapterRegistry.findFactoriesFor(vehicle).stream()
                    .filter(v -> Objects.equals(v.getAdapterFor(vehicle).getClass().getSimpleName(),adapterAttacherDto.getAdapterName()))
                    .findAny();

            if (!factoryOptional.isPresent()) {
                throw new IllegalArgumentException("Can not found the adapter by name [" + adapterAttacherDto.getAdapterName()
                    + "] for vehicle " + vehicle.getName());
            }
            BasicCommunicationAdapter prevAdapter = kernel.findAdapterFor(vehicle.getReference());
            if (prevAdapter != null) {
                prevAdapter.disable();
            }
            kernel.attachAdapterToVehicle(vehicle.getReference(), factoryOptional.get());

            BasicCommunicationAdapter newAdapter = Objects.requireNonNull(kernel.findAdapterFor(vehicle.getReference()));
            if (adapterAttacherDto.isEnable()) {
                newAdapter.enable();
            }
            if (adapterAttacherDto.getInitialPositionUUID() != null && newAdapter instanceof SimCommunicationAdapter) {
                ((SimCommunicationAdapter) newAdapter).initVehiclePosition(adapterAttacherDto.getInitialPositionUUID());
            }

            Map<String, String> item = new LinkedHashMap<>();
            item.put("uuid", vehicle.getUUID());
            item.put("name", vehicle.getName());
            item.put("position_uuid", (vehicle.getCurrentPosition() == null ? "":vehicle.getCurrentPosition().getUUID()));
            item.put("state", vehicle.getState().name());
            if (newAdapter == null) {
                item.put("adapter", "");
                item.put("enable", String.valueOf(false));
            } else {
                item.put("adapter", newAdapter.getClass().getSimpleName());
                item.put("enable", String.valueOf(newAdapter.isEnabled()));
            }
            item.put("adapter_state", vehicle.getAdapterState().name());
            item.put("processing_state", vehicle.getProcState().name());
            updatedVehiclesProfile.add(item);
        }

        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, updatedVehiclesProfile);
    }

    private  TCSResponseEntity<?> dispatchVehicle(final LocalKernel kernel, long sceneId, Object jsonBody) {
        if (jsonBody == null || jsonBody.toString().isEmpty()) {
            throw new IllegalArgumentException("The vehicle can not be dispatched with EMPTY content");
        }

        Vehicle vehicle = Objects.requireNonNull(kernel.getTCSObject(Vehicle.class, jsonBody.toString()));
        kernel.dispatchVehicle(vehicle.getReference(), true);

        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS);
    }

    private  TCSResponseEntity<?> withdrawTObyVehicle(final LocalKernel kernel, long sceneId, Object jsonBody) throws IOException {
        if (jsonBody == null || jsonBody.toString().isEmpty()) {
            throw new IllegalArgumentException("The vehicle can not be dispatched with EMPTY content");
        }

        String json = objectMapper.writeValueAsString(jsonBody);
        TransportWithdrawDto transportWithdrawDto = objectMapper.readValue(json, TransportWithdrawDto.class);

        Vehicle vehicle = Objects.requireNonNull(kernel.getTCSObject(Vehicle.class, jsonBody.toString()));
        kernel.withdrawTransportOrderByVehicle(vehicle.getReference(), transportWithdrawDto.isDisableVehicle());

        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS);
    }

    private  TCSResponseEntity<?> parkVehicle(final LocalKernel kernel, long sceneId, Object jsonBody) throws Exception {
        if (jsonBody == null || jsonBody.toString().isEmpty()) {
            throw new IllegalArgumentException("The vehicle can not be dispatched with EMPTY content");
        }

        //TODO:
        throw new Exception("TO BE SUPPORTED");
//        Vehicle vehicle = Objects.requireNonNull(kernel.getTCSObject(Vehicle.class, jsonBody.toString()));
    }
}
