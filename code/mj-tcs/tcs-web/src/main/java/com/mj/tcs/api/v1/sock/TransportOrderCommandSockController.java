package com.mj.tcs.api.v1.sock;

import com.mj.tcs.LocalKernel;
import com.mj.tcs.api.v1.dto.LocationDto;
import com.mj.tcs.api.v1.dto.LocationTypeDto;
import com.mj.tcs.api.v1.dto.SceneDto;
import com.mj.tcs.api.v1.dto.communication.TCSRequestEntity;
import com.mj.tcs.api.v1.dto.communication.TCSResponseEntity;
import com.mj.tcs.api.v1.web.ServiceController;
import com.mj.tcs.data.base.TCSObjectReference;
import com.mj.tcs.data.model.Location;
import com.mj.tcs.data.model.LocationType;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.data.order.DriveOrder;
import com.mj.tcs.data.order.TransportOrder;
import com.mj.tcs.service.ServiceGateway;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Wang Zhen
 */
@Controller
public class TransportOrderCommandSockController extends ServiceController {

    @MessageMapping("/topic/actions/scenes/{sceneId}/torders")
    @SendToUser("/topic/actions/response")
    public TCSResponseEntity<?> executeAction(@DestinationVariable Long sceneId,
                                              TCSRequestEntity request,
                                              SimpMessageHeaderAccessor ha) {
        Objects.requireNonNull(sceneId);
        Objects.requireNonNull(request);
        TCSResponseEntity<?> responseEntity = null;

        TCSRequestEntity.Action actionCode = request.getActionCode();
        switch (actionCode) {
            case TO_NEW:
                responseEntity = createTransportOrder(sceneId, request.getBody());
                break;
            default:
                responseEntity = new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR,
                        "The action code [" + actionCode + "] is not recognized.");
                break;
        }

        responseEntity.setResponseUUID(request.getRequestUUID());
        return responseEntity;
    }

    private TCSResponseEntity<?> createTransportOrder(long sceneId, Object jsonBody) {
        if (jsonBody == null) {
            return new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR, null,
                                           "The transport order can not created with EMPTY content");
        }

        final ServiceGateway serviceGateway = getService();
        synchronized (serviceGateway) {
            final LocalKernel kernel = serviceGateway.getKernel(sceneId);
            if (kernel == null || !serviceGateway.isSceneDtoRunning(sceneId)) {
                return new TCSResponseEntity<>(TCSResponseEntity.Status.WARNING, null,
                        "The scene of id [" + sceneId +"] is NOT running!");
            }

            final SceneDto sceneDto = serviceGateway.getSceneDto(sceneId);

            String locUUID = "xxx";// TODO
            String destOperation = "NOP"; // TODO
//            Map<String, String> destProperties // TODO:
            // transport order
            List<DriveOrder.Destination> destinations = new ArrayList<>();
            Class<?> locClass = ((sceneDto.getLocationDtoByUUID(locUUID) == null) ?
                    ((sceneDto.getPointDtoByUUID(locUUID) == null) ? null : Point.class) : Location.class);
            if (locClass == null) {
                return new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR, null,
                        "The element by UUID [" + locUUID + "] is neither Point class nor Location class in scene of id [" + sceneId +"]!");
            } else if (Point.class.equals(locClass)) {
                destinations.add(new DriveOrder.Destination(TCSObjectReference.getDummyReference(Point.class, locUUID), "MOVE"));
            } else { // location
                final LocationDto locationDto = sceneDto.getLocationDtoByUUID(locUUID);
                final LocationTypeDto locationTypeDto = Objects.requireNonNull(sceneDto.getLocationTypeDtoByUUID(locationDto.getLocationTypeDto().getUUID()));
                // Get LocationType from kernel
                LocationType locationType = Objects.requireNonNull(kernel.getTCSObject(LocationType.class, locationTypeDto.getUUID()));

                // Convert to location
                Location location = new Location(locUUID, locationDto.getName(), locationType.getReference());

                // Add to the destinations
                destinations.add(new DriveOrder.Destination(location.getReference(),destOperation));
            }

            TransportOrder torder = kernel.createTransportOrder(destinations);
            kernel.activateTransportOrder(torder.getReference());
            // TODO:
            return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, torder, null);
        }
    }

    private DriveOrder.Destination createDestination(final SceneDto sceneDto,
                                                     final String locUUID,
                                                     final String destOperation,
                                                     final Map<String, String> destProperties) throws Exception {
        Objects.requireNonNull(locUUID);
        Objects.requireNonNull(destOperation);
        Objects.requireNonNull(destProperties);

        // TODO:
        throw new Exception("TODO");
        // transport order
//        Class<?> locClass = ((sceneDto.getLocationDtoByUUID(locUUID) == null) ?
//                ((sceneDto.getPointDtoByUUID(locUUID) == null) ? null : Point.class) : Location.class);
//        if (locClass == null) {
//            return new TCSResponseEntity<>(TCSResponseEntity.Status.ERROR, null,
//                    "The element by UUID [" + locUUID + "] is neither Point class nor Location class in scene of id [" + sceneId +"]!");
//        } else if (Point.class.equals(locClass)) {
//            destinations.add(new DriveOrder.Destination(TCSObjectReference.getDummyReference(Point.class, locUUID), "MOVE"));
//        } else { // location
//            final LocationDto locationDto = sceneDto.getLocationDtoByUUID(locUUID);
//            final LocationTypeDto locationTypeDto = Objects.requireNonNull(sceneDto.getLocationTypeDtoByUUID(locationDto.getLocationTypeDto().getUUID()));
//            // Get LocationType from kernel
//            LocationType locationType = Objects.requireNonNull(kernel.getTCSObject(LocationType.class, locationTypeDto.getUUID()));
//
//            // Convert to location
//            Location location = new Location(locUUID, locationDto.getName(), locationType.getReference());
//
//            // Add to the destinations
//            destinations.add(new DriveOrder.Destination(location.getReference(),destOperation));
//        }
    }
}
