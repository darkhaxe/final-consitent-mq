import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @author haze
 * @date created at 2018/4/13 下午12:55
 */
public class Producer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException, UnsupportedEncodingException {
        sendMsg();
    }

    private static void sendMsg() throws MQClientException, UnsupportedEncodingException, RemotingException, MQBrokerException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("producerGroup");
        producer.setNamesrvAddr("192.168.198.16:9876");//192.168.198.16:9876
        producer.start();
        try {
            for (int i = 0; i < 10; i++) {
                Message msg = new Message("testTopic",
                        "tag",
                        ("producer send msg:" + i).getBytes("UTF-8"));
                SendResult res = producer.send(msg);
                System.out.println(res);
            }
        }finally {
            producer.shutdown();
        }
    }


}

class Consumer {
    public static void main(String[] args) throws MQClientException,
            RemotingException, InterruptedException, MQBrokerException, UnsupportedEncodingException {
        consumeMsg();
    }

    private static void consumeMsg() throws MQClientException,
            UnsupportedEncodingException, RemotingException, MQBrokerException, InterruptedException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ConsumerGroup");
        consumer.setNamesrvAddr("192.168.198.16:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("testTopic", "*");
        consumer.registerMessageListener(
                (MessageListenerConcurrently) (msgs, context) -> {
                    msgs.forEach(msg -> System.out.println("consume:" + msg));
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                });
        consumer.start();
        System.out.println("start consume");
    }
}


