package com.hk.reactive.webflux.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author : HK意境
 * @ClassName : HeartHealthHandler
 * @date : 2022/12/16 14:28
 * @description : 心跳健康检查
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Slf4j
@Component
public class HeartHealthHandler implements WebSocketHandler {


    // 实现 WebSocket 支持
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        log.info("WebSocket 客户端心跳信息:{}", session.getHandshakeInfo().getUri());

        return session.send(
                session.receive()
                        .map(message ->
                                session.textMessage("【heart bear】" + message.getPayloadAsText())));
    }


    private void handleMessage(WebSocketMessage message) {


    }

}
