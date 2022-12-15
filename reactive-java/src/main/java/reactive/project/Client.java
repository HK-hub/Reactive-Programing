package reactive.project;

import reactive.project.book.Book;
import reactive.project.message.Message;
import reactive.project.message.MessageSubscriber;
import reactive.project.processor.MessageProcessor;
import reactive.project.subscribe.BookSubscriber;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

/**
 * @author : HK意境
 * @ClassName : Client
 * @date : 2022/12/15 20:40
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class Client {


    public static void test1() throws InterruptedException {
        // 数据发布者
        SubmissionPublisher<Book> publisher = new SubmissionPublisher<>();

        // 建立订阅关系
        BookSubscriber bookSubscriber = new BookSubscriber();
        publisher.subscribe(bookSubscriber);

        // 获取数据,实际的数据可能来自于数据库，网络等
        List<Book> books = Book.getBooks();

        // 提交数据
        for (Book book : books) {
            publisher.submit(book);
        }


        // 应该实现数据的等待处理
        while (bookSubscriber.getCounter() != books.size()) {
            // 等待数据量消费完
            TimeUnit.SECONDS.sleep(1);
        }

        // 关闭
        publisher.close();
    }


    public static void test2() throws InterruptedException {

        // 处理器
        MessageProcessor messageProcessor = new MessageProcessor();
        messageProcessor.setConverter(Message::new);

        // 数据发布者
        SubmissionPublisher<Book> publisher = new SubmissionPublisher<>();

        // 消息订阅者
        MessageSubscriber messageSubscriber = new MessageSubscriber();
        messageProcessor.subscribe(messageSubscriber);

        // 图书订阅者
        BookSubscriber bookSubscriber = new BookSubscriber();
        //publisher.subscribe(bookSubscriber);

        // 建立订阅关系
        publisher.subscribe(messageProcessor);


        // 获取数据,实际的数据可能来自于数据库，网络等
        List<Book> books = Book.getBooks();

        for (Book book : books) {
            TimeUnit.SECONDS.sleep(1);

            publisher.submit(book);
        }


        while (books.size() != messageSubscriber.getCounter()) {
            TimeUnit.SECONDS.sleep(1);
        }

        publisher.close();

    }

    public static void main(String[] args) throws InterruptedException {


        test2();
    }

}
