package com.mj.tcs.api.v1.sock;

import com.mj.tcs.api.v1.web.ServiceController;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Wang Zhen
 */
@Controller
//@RequestMapping({"/api/v1", ""})
public class OperatingMessageGeneratorController extends ServiceController {

//    @RequestMapping("/hello")
//    @MessageMapping("/hello")
//    public String test(Action message) throws Exception {
//        System.out.println(message);
//        return "index";
//    }


//    @RequestMapping("/model")
    @MessageMapping("/topic/greetings")
//    @SendTo("/topic/upload_model_result")
    public void receiveModel(Map<String, String> message) throws Exception {
        System.out.println("receiveModel: " + message);
        getMessageSender().convertAndSend("/topic/greetings","receiveModel");
    }

//    @Scheduled(fixedRate = 5000)
    public void pushMessage() throws Exception {
//        System.out.println("xxx");
        getMessageSender().convertAndSend("/topic/greetings","pushMessage");
    }

    @MessageMapping("/scene/{sceneId}/vehicles")
//    @SendTo("/topic/upload_model_result")
    public void test2(@DestinationVariable Long sceneId, Map<String, String> message) throws Exception {
        System.out.println(sceneId);
        System.out.println(message);
    }

    public static class Action {
        private Map<String, String> properties = new LinkedHashMap<>();

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
    }
}
