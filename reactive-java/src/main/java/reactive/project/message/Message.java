package reactive.project.message;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author : HK意境
 * @ClassName : Message
 * @date : 2022/12/15 20:59
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
@Data
@ToString
public class Message {
    private LocalDateTime time = LocalDateTime.now();
    private String json;

    public Message(String json) {
        this.json = json;
    }

    public Message(Object object) {
        this.json = JSON.toJSONString(object);
    }

}
