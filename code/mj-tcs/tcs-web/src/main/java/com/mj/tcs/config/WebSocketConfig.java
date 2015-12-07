package com.mj.tcs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wang Zhen
 */
@Configuration
//@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer
        /*implements WebSocketConfigurer*/ {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp").setAllowedOrigins("*")
//                .setHandshakeHandler(new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy()))
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/init");
        config.setApplicationDestinationPrefixes("/app");
    }

//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(createTcsWebSocketHandler(), "/myHandler")
//                .addInterceptors(new HttpSessionHandshakeInterceptor());
//    }
//
//    @Bean
//    public TextWebSocketHandler createTcsWebSocketHandler() {
//        return new TcsWebSocketHandler();
//    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }

    static class TcsWebSocketHandler extends TextWebSocketHandler {
        private static final Map<String, WebSocketSession> users;

        static {
            users = new HashMap<>();
        }

        /**
         * 连接成功时候，会触发UI上onopen方法
         */
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            System.out.println("connect to the websocket success......");
            users.put(session.getId(), session);
            //这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
            //TextMessage returnMessage = new TextMessage("你将收到的离线");
            //session.sendMessage(returnMessage);
        }

        /**
         * 在UI在用js调用websocket.send()时候，会调用该方法
         */
        @Override
        protected void handleTextMessage(WebSocketSession session,
                                         TextMessage message) throws Exception {
            super.handleTextMessage(session, message);
        }

        /**
         * 给某个用户发送消息
         *
         * @param userName
         * @param message
         */
        public void sendMessageToUser(String userName, TextMessage message) {
            for (WebSocketSession user : users.values()) {
//                if (user.getAttributes().get(Constants.WEBSOCKET_USERNAME).equals(userName)) {
//                    try {
//                        if (user.isOpen()) {
//                            user.sendMessage(message);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                }
            }
        }

        /**
         * 给所有在线用户发送消息
         *
         * @param message
         */
        public void sendMessageToUsers(TextMessage message) {
            for (WebSocketSession user : users.values()) {
//                try {
//                    if (user.isOpen()) {
//                        user.sendMessage(message);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }

        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
            if(session.isOpen()){
                session.close();
            }
//            logger.debug("websocket connection closed......");
            users.remove(session.getId());
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
//            logger.debug("websocket connection closed......");
            users.remove(session.getId());
        }

        @Override
        public boolean supportsPartialMessages() {
            return false;
        }
    }
}