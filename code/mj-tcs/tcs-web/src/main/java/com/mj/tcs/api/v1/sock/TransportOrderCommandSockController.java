package com.mj.tcs.api.v1.sock;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.tcs.LocalKernel;
import com.mj.tcs.access.orders.Destination;
import com.mj.tcs.access.orders.Transport;
import com.mj.tcs.api.v1.dto.TransportWithdrawDto;
import com.mj.tcs.api.v1.dto.communication.TCSRequestEntity;
import com.mj.tcs.api.v1.dto.communication.TCSResponseEntity;
import com.mj.tcs.api.v1.web.ServiceController;
import com.mj.tcs.data.ObjectUnknownException;
import com.mj.tcs.data.base.TCSObjectReference;
import com.mj.tcs.data.model.Location;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.model.Vehicle;
import com.mj.tcs.data.order.DriveOrder;
import com.mj.tcs.data.order.TransportOrder;
import com.mj.tcs.service.ServiceGateway;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;

/**
 * @author Wang Zhen
 */
@Controller
public class TransportOrderCommandSockController extends ServiceController {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @MessageMapping("/topic/actions/scenes/{sceneId}/torders/request")
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
                case TO_NEW:
                    responseEntity = createTransportOrder(kernel, sceneId, request.getBody());
                    break;
                case TO_WITHDRAW:
                    responseEntity = withdrawTransportOrder(kernel, sceneId, request.getBody());
                    break;
                default:
                    throw new IllegalArgumentException("The action code [" + actionCode + "] is not recognized.");
            }
        } catch (Exception e) {
            responseEntity = new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR,null,e.getMessage());
        }

        responseEntity.setResponseUUID(request.getRequestUUID());
        return responseEntity;
    }

    private TCSResponseEntity<?> createTransportOrder(final LocalKernel kernel, long sceneId, Object jsonBody) throws Exception {
        if (jsonBody == null || jsonBody.toString().isEmpty()) {
            throw new IllegalArgumentException("The transport order can not created with EMPTY content");
        }

        Transport transport = null;
        String json = objectMapper.writeValueAsString(jsonBody);
        transport = objectMapper.readValue(json, Transport.class);
        assert transport != null;

        TransportOrder order = createTransportOrder(kernel, transport.getDestinations());

        // Set the transport order's deadline, if any.
        if (transport.getDeadline() != null) {
            final long deadline = transport.getDeadline().getTime();
            kernel.setTransportOrderDeadline(order.getReference(), deadline);
        }

        // Set the order's intended vehicle, if any.
        setIntendedVehicle(kernel, order, transport.getIntendedVehicle());

        // Set the transport order's dependencies, if any.
        setDependencies(kernel, order, transport.getDependencies());

        // Activate the new transport order.
        kernel.activateTransportOrder(order.getReference());

        // TODO: Save to Database.
        // Everything went fine - let the client know.
        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, order, transport.getUuid());
    }


    private TCSResponseEntity<?> withdrawTransportOrder(final LocalKernel kernel, long sceneId, Object jsonBody) throws IOException {
        if (jsonBody == null || jsonBody.toString().isEmpty()) {
            throw new IllegalArgumentException("The transport order can not withdraw with EMPTY content");
        }

        TransportWithdrawDto transportWithdrawDto = null;
        String json = objectMapper.writeValueAsString(jsonBody);
        transportWithdrawDto = objectMapper.readValue(json, TransportWithdrawDto.class);
        assert transportWithdrawDto != null;

        TransportOrder transportOrder = Objects.requireNonNull(kernel.getTCSObject(TransportOrder.class, transportWithdrawDto.getUUID()));
        boolean force = transportWithdrawDto.isForce();
        boolean disableVehicle = transportWithdrawDto.isDisableVehicle();
        kernel.withdrawTransportOrder(transportOrder.getReference(), disableVehicle);
        if (force) {
            kernel.withdrawTransportOrder(transportOrder.getReference(), disableVehicle);
        }

        // TODO: Save to Database.
        // Everything went fine - let the client know.
        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS);
    }

    /**
     * Creates a transport order.
     *
     * @param kernel The kernel instance
     * @param destinations The destinations of this order.
     * @return The newly created transport order.
     */
    private TransportOrder createTransportOrder(final LocalKernel kernel, List<Destination> destinations) {
        List<DriveOrder.Destination> realDests = new LinkedList<>();

        for (Destination curDest : destinations) {
            final String locUUID = Objects.requireNonNull(curDest.getLocationUUID());
            Class<?> locClass = ((kernel.getTCSObject(Location.class, locUUID) == null) ?
                    ((kernel.getTCSObject(Point.class, locUUID) == null) ? null : Point.class) : Location.class);

            if (locClass == null) {
                throw new ObjectUnknownException("The element by UUID [" + locUUID + "] is neither Point class nor Location class!");
            } else if (Point.class.equals(locClass)) {
                // TODO: Refine const "MOVE"
                realDests.add(new DriveOrder.Destination(TCSObjectReference.getDummyReference(Point.class, locUUID), "MOVE"));
            } else { // location
                String curDestOp = curDest.getOperation();
                // Get Location from kernel
                final Location location = Objects.requireNonNull(kernel.getTCSObject(Location.class, locUUID));

                // Add to the destinations
                realDests.add(new DriveOrder.Destination(location.getReference(),curDestOp));
            }
        }
        return kernel.createTransportOrder(realDests);
    }

    /**
     * Sets the intended vehicle.
     *
     * @param kernel The kernel instance
     * @param order The transport order the vehicle shall be set.
     * @param vehicleUUID The name of the vehicle.
     */
    void setIntendedVehicle(final LocalKernel kernel, TransportOrder order, String vehicleUUID) {
        if (vehicleUUID != null && !vehicleUUID.isEmpty()) {
            Vehicle vehicle = kernel.getTCSObject(Vehicle.class, vehicleUUID);
            if (vehicle == null) {
                // TODO: Set state of created order to FAILED?
                throw new ObjectUnknownException("Unknown vehicle: " + vehicleUUID);
            }
            kernel.setTransportOrderIntendedVehicle(order.getReference(),
                    vehicle.getReference());
        }
    }

    /**
     * Sets a list of dependencies to a transport order.
     *
     * @param kernel The kernel instance
     * @param order The order.
     * @param deps The list of dependencies.
     */
    private void setDependencies(final LocalKernel kernel, TransportOrder order, List<String> deps) {
        if (deps == null) {
            return;
        }

        for (String curDepUUID : deps) {
            TransportOrder curDep = kernel.getTCSObject(TransportOrder.class,
                    curDepUUID);
            // If curDep is null, ignore it - it might have been processed and
            // removed already.
            if (curDep != null) {
                kernel.addTransportOrderDependency(order.getReference(),
                        curDep.getReference());
            }
        }
    }
}
