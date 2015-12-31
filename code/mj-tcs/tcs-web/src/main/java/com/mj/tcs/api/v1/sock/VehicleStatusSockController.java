package com.mj.tcs.api.v1.sock;

import com.google.common.eventbus.Subscribe;
import com.mj.tcs.api.v1.dto.VehicleStatusDto;
import com.mj.tcs.access.status.VehicleStatusMessage;
import com.mj.tcs.api.v1.web.ServiceController;
import org.springframework.stereotype.Controller;

/**
 * @author Wang Zhen
 */
@Controller
public class VehicleStatusSockController extends ServiceController {

    /**
     * // /topic/scenes/{sceneId}/vehicles <--> VehicleStatusDto
     *    /topic/scenes/{sceneId}/TOs <--> TransportOrderStatusDto
     *  TODO
     */
    @Subscribe
    public void receiveAndPubVehicleStatusMessage(VehicleStatusMessage message) {
        if (message == null) {
            return;
        }

        synchronized (message) {
            final long sceneId = message.getSceneId();
            VehicleStatusDto statusDto = new VehicleStatusDto();
            statusDto.setUUID(message.getVehicleUUID());
            statusDto.setPosition(message.getPositionUUID());
            statusDto.setProcessingState(message.getProcState().name());
            statusDto.setState(message.getState().name());

            getMessageSender().convertAndSend("/topic/status/scenes/" + sceneId + "/vehicles", statusDto);
        }
    }
}
