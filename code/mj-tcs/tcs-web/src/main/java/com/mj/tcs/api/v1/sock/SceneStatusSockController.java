package com.mj.tcs.api.v1.sock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.tcs.api.v1.web.ServiceController;
import org.springframework.stereotype.Controller;

/**
 * @author Wang Zhen
 */
@Controller
public class SceneStatusSockController extends ServiceController {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // TODO: NO NEED ???
//    @Subscribe
//    public void receiveAndPubSceneStatusMessage(StatusMessage message) {
//        if (message == null) {
//            return;
//        }
//
//        final long sceneId = message.getSceneId();
//
//        if (message instanceof VehicleStatusMessage) {
//            VehicleStatusMessage vehicleStatusMessage = (VehicleStatusMessage) message;
//
//            VehicleStatusDto statusDto = new VehicleStatusDto();
//            statusDto.setUUID(vehicleStatusMessage.getVehicleUUID());
//            statusDto.setPosition(vehicleStatusMessage.getPosition());
//            statusDto.setProcessingState(vehicleStatusMessage.getProcState().name());
//            statusDto.setState(vehicleStatusMessage.getState().name());
//
//            getMessageSender().convertAndSend("/topic/status/scenes/" + sceneId, statusDto);
//        } else if (message instanceof OrderStatusMessage) {
//            OrderStatusMessage orderStatusMessage = (OrderStatusMessage) message;
//            // TODO: convert to TransportOrder DTO
//            try {
//                String content = objectMapper.writeValueAsString(message);
//
//                getMessageSender().convertAndSend("/topic/status/scenes/" + sceneId, content);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
