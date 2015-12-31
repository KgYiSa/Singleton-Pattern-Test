package com.mj.tcs.api.v1.sock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;
import com.mj.tcs.api.v1.dto.TransportOrderStatusDto;
import com.mj.tcs.access.status.OrderStatusMessage;
import com.mj.tcs.api.v1.web.ServiceController;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wang Zhen
 */
@Controller
public class TransportOrderStatusSockController extends ServiceController {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Subscribe
    public void receiveAndPubTOStatusMessage(OrderStatusMessage message) {
        if (message == null) {
            return;
        }

        synchronized (message) {
            final long sceneId = message.getSceneId();
            // TODO: convert to TransportOrder DTO
            final TransportOrderStatusDto orderStatusDto = new TransportOrderStatusDto();
            orderStatusDto.setUUID(message.getOrderUUID());
            orderStatusDto.setExecutingVehicle(message.getExecutingVehicleUUID());
            orderStatusDto.setOrderState(message.getOrderState().name());
            final List<TransportOrderStatusDto.DestinationStatusDto> destinationStatusDtos = new ArrayList<>();
            if (message.getDestinations() != null) {
                message.getDestinations().forEach(v -> {
                    final TransportOrderStatusDto.DestinationStatusDto destinationStatusDto = new TransportOrderStatusDto.DestinationStatusDto();
                    destinationStatusDto.setLocationUUID(v.getLocationUUID());
                    destinationStatusDto.setOperation(v.getOperation());
                    destinationStatusDto.setState(v.getState().name());
                    destinationStatusDtos.add(destinationStatusDto);
                });
            }
            orderStatusDto.setDestinationDtos(destinationStatusDtos);

            getMessageSender().convertAndSend("/topic/status/scenes/" + sceneId+"/torders", orderStatusDto);
        }
    }
}
