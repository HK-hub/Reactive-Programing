package com.hk.reactive.webflux.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author : HK意境
 * @ClassName : MessageHandler
 * @date : 2022/12/15 23:05
 * @description : WebFlux 处理模块
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
public class MessageHandler {

    // 在 WebFlux 里面进行响应得话会区分单个响应(单个对象)以及多个响应(集合)

    /**
     * 请求接收以及响应
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> echoHandler(ServerRequest serverRequest) {

        Mono<ServerResponse> responseMono = ServerResponse.ok()
                .header("Content-Type", "text/html;charset=UTF-8")
                .body(BodyInserters.fromValue("spring webflux 响应式编程"));


        return responseMono;
    }



}
