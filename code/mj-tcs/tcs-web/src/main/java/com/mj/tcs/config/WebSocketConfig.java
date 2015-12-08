package com.mj.tcs.config;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Wang Zhen
 */
@Configuration
//@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer
        /*implements ApplicationListener*/
        /*implements WebSocketConfigurer*/ {

    public static final String SESSION_ATTR = "USER";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp").setAllowedOrigins("*")
//                .setHandshakeHandler(new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy()))
//                .setHandshakeHandler(new MyHandshakeHandler())
                .withSockJS()
                .setInterceptors();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/init");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user/");
    }

    public class HttpSessionIdHandshakeInterceptor implements HandshakeInterceptor {

        public boolean beforeHandshake(ServerHttpRequest request,
                                       ServerHttpResponse response,
                                       WebSocketHandler wsHandler,
                                       Map<String, Object> attributes)
                throws Exception {
            if (request instanceof ServletServerHttpRequest) {
                ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                HttpSession session = servletRequest.getServletRequest().getSession(false);
                if (session != null) {
                    attributes.put(SESSION_ATTR, session.getId());
                }
            }
            return true;
        }

        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Exception ex) {
        }
    }

//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(createTcsWebSocketHandler(), "/handler")
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

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        /* SAME AS:

        <filter>
         <filter-name>SomeFilter</filter-name>
            <filter-class>com.somecompany.SomeFilter</filter-class>
        </filter>
        <filter-mapping>
            <filter-name>SomeFilter</filter-name>
            <url-pattern>/url/*</url-pattern>
            <init-param>
               <param-name>paramName</param-name>
               <param-value>paramValue</param-value>
            </init-param>
        </filter-mapping>
        * */
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setFilter(new OpenEntityManagerInViewFilter());
        filterRegistrationBean.setName("Spring OpenEntityManagerInViewFilter");
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

//    public class MyHandshakeHandler extends DefaultHandshakeHandler {
//
//        @Override
//        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
//                                          Map<String, Object> attributes) {
//            // add your own code to determine the user
//            return null;
//        }
//    }
//
//    @Override
//    public void onApplicationEvent(ApplicationEvent event) {
//        if (event instanceof SessionConnectedEvent) {
//            System.out.println("New session connected...");
//        } else if (event instanceof SessionDisconnectEvent) {
//            System.out.println("One session disconnected...");
//        }
//    }


//    static class TcsWebSocketHandler extends TextWebSocketHandler {
//        private static final Map<String, WebSocketSession> users;
//
//        static {
//            users = new HashMap<>();
//        }
//
//        /**
//         * 连接成功时候，会触发UI上onopen方法
//         */
//        @Override
//        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//            System.out.println("connect to the websocket success......");
//            users.put(session.getId(), session);
//            //这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
//            //TextMessage returnMessage = new TextMessage("你将收到的离线");
//            //session.sendMessage(returnMessage);
//        }
//
//        /**
//         * 在UI在用js调用websocket.send()时候，会调用该方法
//         */
//        @Override
//        protected void handleTextMessage(WebSocketSession session,
//                                         TextMessage message) throws Exception {
//            super.handleTextMessage(session, message);
//        }
//
//        /**
//         * 给某个用户发送消息
//         *
//         * @param userName
//         * @param message
//         */
//        public void sendMessageToUser(String userName, TextMessage message) {
//            for (WebSocketSession user : users.values()) {
////                if (user.getAttributes().get(Constants.WEBSOCKET_USERNAME).equals(userName)) {
////                    try {
////                        if (user.isOpen()) {
////                            user.sendMessage(message);
////                        }
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                    break;
////                }
//            }
//        }
//
//        /**
//         * 给所有在线用户发送消息
//         *
//         * @param message
//         */
//        public void sendMessageToUsers(TextMessage message) {
//            for (WebSocketSession user : users.values()) {
////                try {
////                    if (user.isOpen()) {
////                        user.sendMessage(message);
////                    }
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//            }
//        }
//
//        @Override
//        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//            if(session.isOpen()){
//                session.close();
//            }
////            logger.debug("websocket connection closed......");
//            users.remove(session.getId());
//        }
//
//        @Override
//        public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
////            logger.debug("websocket connection closed......");
//            users.remove(session.getId());
//        }
//
//        @Override
//        public boolean supportsPartialMessages() {
//            return false;
//        }
//    }
}