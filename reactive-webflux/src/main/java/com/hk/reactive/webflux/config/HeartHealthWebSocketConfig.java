package com.hk.reactive.webflux.config;

import com.hk.reactive.webflux.websocket.HeartHealthHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : HeartHealthWebSocketConfig
 * @date : 2022/12/16 14:39
 * @description : 配置 WebSocket
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Configuration
public class HeartHealthWebSocketConfig {

    // 配置 handler mapping
    @Bean
    public HandlerMapping heartBeatWebSocketMapping(@Autowired HeartHealthHandler handler) {

        Map<String, WebSocketHandler> config = new HashMap<>();
        // 配置映射模式
        config.put("/websocket/{token}", handler);
        // 映射处理
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        simpleUrlHandlerMapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        // 映射路径
        simpleUrlHandlerMapping.setUrlMap(config);

        return simpleUrlHandlerMapping;
    }


    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }


}
