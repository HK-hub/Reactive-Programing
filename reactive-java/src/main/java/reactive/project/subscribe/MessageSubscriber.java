package reactive.project.subscribe;

import reactive.project.message.Message;

import java.util.concurrent.Flow;

/**
 * @author : HK意境
 * @ClassName : MessageSubscriber
 * @date : 2022/12/15 21:24
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class MessageSubscriber implements Flow.Subscriber<Message> {

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(Message item) {
        System.out.println(item);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
