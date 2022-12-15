package reactive.project.message;

import lombok.Getter;

import java.util.concurrent.Flow;

/**
 * @author : HK意境
 * @ClassName : MessageSubscriber
 * @date : 2022/12/15 20:56
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Getter
public class MessageSubscriber implements Flow.Subscriber<Message>{

    private Flow.Subscription subscription;
    private int counter ;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;

        // 一次性接收的最大数据流，如果不足n 个数据流也接收
        subscription.request(1);
        String message = """
                ferwgreg +
                gregregre +
                gregre +
                """;
    }

    @Override
    public void onNext(Message item) {
        System.out.println(item);
        this.counter++;
        this.subscription.request(1);
    }


    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("complete: " + counter);
    }
}
