package com.mj.tcs.api.sock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;
import com.mj.tcs.access.status.OrderStatusMessage;
import com.mj.tcs.api.dto.TransportOrderDto;
import com.mj.tcs.api.web.ServiceController;
import com.mj.tcs.data.order.TransportOrder;
import org.springframework.stereotype.Controller;

import java.util.Objects;

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
            final TransportOrder.State orderState = message.getOrderState();
//            final TransportOrderStatusDto orderStatusDto = new TransportOrderStatusDto();
//            orderStatusDto.setUUID(message.getOrderUUID());
//            orderStatusDto.setExecutingVehicle(message.getExecutingVehicleUUID());
//            orderStatusDto.setOrderState(orderState.name());
//            final List<TransportOrderStatusDto.DestinationStatusDto> destinationStatusDtos = new ArrayList<>();
//            if (message.getDestinations() != null) {
//                message.getDestinations().forEach(v -> {
//                    final TransportOrderStatusDto.DestinationStatusDto destinationStatusDto = new TransportOrderStatusDto.DestinationStatusDto();
//                    destinationStatusDto.setLocationUUID(v.getLocationUUID());
//                    destinationStatusDto.setOperation(v.getOperation());
//                    destinationStatusDto.setState(v.getState().name());
//                    destinationStatusDtos.add(destinationStatusDto);
//                });
//            }
//            orderStatusDto.setDestinationDtos(destinationStatusDtos);

            final TransportOrderDto orderDto = new TransportOrderDto();
            orderDto.setUUID(message.getOrderUUID());
            orderDto.setExecutingVehicle(message.getExecutingVehicleUUID());
            orderDto.setOrderState(orderState.name());

            if (message.getDestinations() != null) {
                message.getDestinations().forEach(v -> {
                    final TransportOrderDto.DestinationDto dest = new TransportOrderDto.DestinationDto();
                    dest.setLocationUUID(v.getLocationUUID());
                    // No need to update the other parameter, it will let service to handle it!
                    dest.setState(v.getState().name());
                    orderDto.addDestionation(dest);
                });
            }

            // Save or update for the TransportOrderStatusDto when it is not in raw_state & processing_state
            if (!Objects.equals(TransportOrder.State.RAW, orderState) && !Objects.equals(TransportOrder.State.BEING_PROCESSED, orderState)) {
                try {
                    getService().updateTrnasportOrderDto(sceneId, orderDto);
                } catch (Exception e) {
                    //TODO: add exception handling
                    e.printStackTrace();
                    return;
                }
            }

            getMessageSender().convertAndSend("/topic/status/scenes/" + sceneId+"/torders", orderDto);
        }
    }
}
