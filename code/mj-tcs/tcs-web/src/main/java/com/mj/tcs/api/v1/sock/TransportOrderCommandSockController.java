package com.mj.tcs.api.v1.sock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.tcs.LocalKernel;
import com.mj.tcs.access.orders.Destination;
import com.mj.tcs.access.orders.Transport;
import com.mj.tcs.api.v1.dto.DestinationDto;
import com.mj.tcs.api.v1.dto.TransportOrderDto;
import com.mj.tcs.api.v1.dto.TransportWithdrawDto;
import com.mj.tcs.api.v1.dto.base.EntityAuditorDto;
import com.mj.tcs.api.v1.dto.communication.TCSRequestEntity;
import com.mj.tcs.api.v1.dto.communication.TCSResponseEntity;
import com.mj.tcs.api.v1.web.ServiceController;
import com.mj.tcs.data.ObjectUnknownException;
import com.mj.tcs.data.model.Location;
import com.mj.tcs.data.model.Point;
import com.mj.tcs.service.ServiceGateway;
import com.mj.tcs.util.UniqueTimestampGenerator;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            return TCSResponseEntity.getBuilder()
                    .setResponseUUID(request.getRequestUUID())
                    .setStatus(TCSResponseEntity.Status.ERROR)
                    .setStatusMessage("The action code is null.")
                    .get();
        }

        // check the scene is running.
        final ServiceGateway serviceGateway = getService();
        final LocalKernel kernel = serviceGateway.getKernel(sceneId);
        if (kernel == null || !serviceGateway.isSceneDtoRunning(sceneId)) {
            return TCSResponseEntity.getBuilder()
                    .setResponseUUID(request.getRequestUUID())
                    .setStatus(TCSResponseEntity.Status.WARNING)
                    .setStatusMessage("The scene of id [" + sceneId +"] is NOT running!")
                    .get();
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
            responseEntity = TCSResponseEntity.getBuilder()
                    .setResponseUUID(request.getRequestUUID())
                    .setStatus(TCSResponseEntity.Status.ERROR)
                    .setStatusMessage(e.getMessage())
                    .get();
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

        TransportOrderDto orderDto = new TransportOrderDto();
        orderDto.setSceneId(sceneId);

        // Name
        orderDto.setName("TO-"+new UniqueTimestampGenerator().getNextTimestampInStringFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SS")));

        if (transport.getDeadline() != null) {
            orderDto.setDeadline(transport.getDeadline().getTime());
        }

        orderDto.setIntendedVehicleUUID(transport.getIntendedVehicle());

        // TODO: Update
        orderDto.setAuditorDto(new EntityAuditorDto());

        // Destinations
        final List<DestinationDto> destinationDtos = new ArrayList<>();
        if (transport.getDestinations() != null) {
            for (Destination dest : transport.getDestinations()) {
                final String locUUID = dest.getLocationUUID();
                final String operation = dest.getOperation();
                boolean isPoint = (kernel.getTCSObject(Point.class, locUUID) != null);
                boolean isLocation = (kernel.getTCSObject(Location.class, locUUID) != null);
                if (!isPoint && !isLocation) {
                    throw new ObjectUnknownException("The element by UUID [" + locUUID + "] is neither Point class nor Location class!");
                }

                destinationDtos.add(new DestinationDto(locUUID, operation, isPoint));
            }
        }
        orderDto.setDestinations(destinationDtos);

        orderDto.setDeps(transport.getDependencies());

        orderDto = getService().createTransportOrder(sceneId, orderDto);

        // Everything went fine - let the client know.
        return TCSResponseEntity.getBuilder()
                .setStatus(TCSResponseEntity.Status.SUCCESS)
                .setBody(orderDto)
                .get();
//        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS, orderDto, transport.getUuid());
    }


    private TCSResponseEntity<?> withdrawTransportOrder(final LocalKernel kernel, long sceneId, Object jsonBody) throws Exception {
        if (jsonBody == null || jsonBody.toString().isEmpty()) {
            throw new IllegalArgumentException("The transport order can not withdraw with EMPTY content");
        }

        String json = objectMapper.writeValueAsString(jsonBody);
        TransportWithdrawDto transportWithdrawDto = objectMapper.readValue(json, TransportWithdrawDto.class);
        assert transportWithdrawDto != null;

        getService().withdrawTransportOrder(sceneId, transportWithdrawDto);

        // Everything went fine - let the client know.
        return TCSResponseEntity.getBuilder()
                .setStatus(TCSResponseEntity.Status.SUCCESS)
                .get();
//        return new TCSResponseEntity<>(TCSResponseEntity.Status.SUCCESS);
    }
}
