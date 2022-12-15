package reactive.demo;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author : HK意境
 * @ClassName : Client
 * @date : 2022/12/15 19:32
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class Client {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        List<String> dataList = List.of("com", "hk", "reactive", "demo");
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        // 进行数据消费处理
        CompletableFuture<Void> task = publisher.consume(System.out::println);
        // 以上只是定义了消息生产者和消息消费者之间的基本关联模型，随后进行具体的数据处理


        // 发送提价数据到缓冲区(Publisher 内部有一个缓冲区)
        for (String s : dataList) {
            publisher.submit(s);
            TimeUnit.SECONDS.sleep(1);
        }

        // 数据发送完毕
        publisher.close();

        Void unused = task.get();

        // 缓冲区大小
        System.out.println(publisher.getMaxBufferCapacity());

    }


}
