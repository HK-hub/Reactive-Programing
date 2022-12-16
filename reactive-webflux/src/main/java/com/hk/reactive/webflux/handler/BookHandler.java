package com.hk.reactive.webflux.handler;

import com.alibaba.fastjson.JSONObject;
import com.hk.reactive.webflux.domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @author : HK意境
 * @ClassName : BookHandler
 * @date : 2022/12/15 23:43
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Component
@Slf4j
public class BookHandler {

    public Mono<Book> bookHandler(Book book) {

        log.info("【{}】业务层处理数据：{}", Thread.currentThread().getName(), book);
        book.setContent("新内容：" + book.getContent());
        book.setTitle("新题目：" + book.getTitle());
        //book.setPublishDate(new Date());
        //book.setDescription("新描述：" + book.getDescription());

        // 实现数据的响应
        return Mono.create(monoSink -> monoSink.success(book));
    }


    // 返回集合数据
    public Flux<Book> getAllBooks() {

        List<Book> books = Book.getBooks();

        // 实现了集合的响应
        return Flux.fromIterable(books);
    }


    // 返回Map 集合
    public Flux<Map.Entry> getBooksMap(Book book) {

        Map bookMap = (Map<String, Object>) JSONObject.toJSON(book);

        return Flux.fromIterable(bookMap.entrySet());
    }



}
