package reactive.project.processor;

import reactive.project.book.Book;
import reactive.project.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

/**
 * @author : HK意境
 * @ClassName : MessageProcessor
 * @date : 2022/12/15 21:08
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Getter
@Setter
public class MessageProcessor extends SubmissionPublisher<Message> implements Flow.Processor<Book, Message> {

    // 需要接收
    private Flow.Subscription subscription;
    // 外部转换器
    private Function<Book, Message> converter;


    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(Book item) {
        Message message = this.converter.apply(item);
        this.submit(message);
        this.subscription.request(3);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("complete");
    }

}
