package com.hk.reactive.webflux.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : HK意境
 * @ClassName : Book
 * @date : 2022/12/15 23:41
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@AllArgsConstructor
public class Book {

    private long id;
    private String title;
    private String author;
    private Double price;

    private String content;


    public static List<Book> getBooks() {

        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "深入理解JVM虚拟机", "周志明", 10D, ""));
        books.add(new Book(2, "凤凰架构", "周志明", 10D, ""));
        books.add(new Book(3, "设计模式之禅", "秦小波", 10D, ""));
        books.add(new Book(4, "分布式事务实践", "冰河技术", 10D, ""));
        books.add(new Book(5, "分布式算法导论", "泰尔", 10D, ""));
        books.add(new Book(6, "字节码编程技术", "小傅哥", 10D, ""));
        books.add(new Book(7, "Java高并发核心卷", "尼恩", 10D, ""));
        return books;
    }
    
    
}
