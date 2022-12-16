package com.hk.reactive.webflux.action;

import com.hk.reactive.webflux.domain.Book;
import com.hk.reactive.webflux.handler.BookHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

/**
 * @author : HK意境
 * @ClassName : BookAction
 * @date : 2022/12/15 23:59
 * @description : 路由配置
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@RestController
@RequestMapping("/book")
@Slf4j
public class BookAction {

    // WebFlux 程序处理
    @Resource
    private BookHandler bookHandler;


    @PostMapping()
    public Object addBook(Book book) {
        log.info("Adding book:{}", book);
        return this.bookHandler.bookHandler(book);
    }

    @GetMapping("/all")
    public Object getAllBooks() {

        Flux<Book> allBooks = bookHandler.getAllBooks();
        return allBooks;
    }


    @GetMapping(path = "/map")
    public Object getBookMap(Book book) {

        return bookHandler.getBooksMap(book);
    }



}
