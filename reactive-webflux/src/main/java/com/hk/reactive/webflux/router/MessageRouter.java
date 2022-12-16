package com.hk.reactive.webflux.router;

import com.hk.reactive.webflux.handler.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author : HK意境
 * @ClassName : MessageRouter
 * @date : 2022/12/15 23:19
 * @description : 消息路由处理
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Configuration
public class MessageRouter {

    // 进行路由功能注册
    @Bean
    public RouterFunction<ServerResponse> routeEcho(MessageHandler messageHandler) {
        // 此时绑定的访问模式为GET 请求模式，而后设置了具体的访问地址，/echo ,
        return RouterFunctions
                // 设置请求地址
                .route(RequestPredicates.GET("/echo")
                        // 设置匹配的请求类型
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN))
                // 设置请求处理方法
                , messageHandler::echoHandler);
    }


}
