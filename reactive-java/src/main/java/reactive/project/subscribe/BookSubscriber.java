package reactive.project.subscribe;

import reactive.project.book.Book;
import lombok.Data;

import java.util.concurrent.Flow;

/**
 * @author : HK意境
 * @ClassName : BookSubscriber
 * @date : 2022/12/15 20:30
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
public class BookSubscriber implements Flow.Subscriber<Book> {

    // 整个的订阅控制
    private Flow.Subscription subscription;
    private int counter = 0;

    // 当定订阅的时候
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        // 保存订阅控制操作
        this.subscription = subscription;

        // 触发数据接收操作
        // 从发布者种获取1个数据
        subscription.request(1);
        System.out.println("[BookSubscriber]数据订阅者开启...");
    }


    // 数据接收
    @Override
    public void onNext(Book item) {
        // 计数累计
        this.counter++;
        System.out.println("[BookSubscriber] " + item.toString());

        // 再次接收，继续触发onNext()
        this.subscription.request(1);

    }


    @Override
    public void onError(Throwable throwable) {
        System.out.println("[BookSubscriber] " + throwable.getMessage());

    }

    @Override
    public void onComplete() {
        System.out.println("[BookSubscriber] 数据处理完毕，共计处理数据：" + counter);

    }
}
