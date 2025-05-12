package cn.tedu.charging.order;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitMqTests {

    @Test
    public void publishDirect() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String directExchangeName = "test-add-exchange-0";
        String directRoutingKey0 = "t-a-q-0";
        String directRoutingKey1 = "t-a-q-1";
        String directRoutingKey2 = "t-a-q-2";
        String directQueueName0 = "test-add-queue-0";
        String directQueueName1 = "test-add-queue-1";
        String directQueueName2 = "test-add-queue-2";

        channel.queueDeclare(directQueueName0, false, false, false, null);
        channel.queueDeclare(directQueueName1, false, false, false, null);
        channel.queueDeclare(directQueueName2, false, false, false, null);

        channel.exchangeDeclare(directExchangeName, BuiltinExchangeType.DIRECT);

        channel.queueBind(directQueueName0, directExchangeName, directRoutingKey0);
        channel.queueBind(directQueueName1, directExchangeName, directRoutingKey1);
        channel.queueBind(directQueueName2, directExchangeName, directRoutingKey2);
        for (int i = 0; i < 5; i++) {
            //定义消息
            String message = "helloRabbit" + i;

            channel.basicPublish(directExchangeName,directRoutingKey0,null,message.getBytes());
            channel.basicPublish(directExchangeName,directRoutingKey1,null,message.getBytes());
            channel.basicPublish(directExchangeName,directRoutingKey2,null,message.getBytes());
        }
        //关闭通道
        channel.close();
        //关闭连接
        connection.close();

    }

    //错误的测试用例，测试发布订阅模式
    @Test
    public void publishFanout() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String directExchangeName = "test-add-exchange-1";
        String directRoutingKey3= "t-a-q-3";
        String directRoutingKey4 = "t-a-q-4";
        String directRoutingKey5 = "t-a-q-5";
        String directQueueName3 = "test-add-queue-3";
        String directQueueName4 = "test-add-queue-4";
        String directQueueName5 = "test-add-queue-5";

        channel.queueDeclare(directQueueName3, false, false, false, null);
        channel.queueDeclare(directQueueName4, false, false, false, null);
        channel.queueDeclare(directQueueName5, false, false, false, null);

        channel.exchangeDeclare(directExchangeName, BuiltinExchangeType.FANOUT);

        channel.queueBind(directQueueName3, directExchangeName, directRoutingKey3);
        channel.queueBind(directQueueName4, directExchangeName, directRoutingKey4);
        channel.queueBind(directQueueName5, directExchangeName, directRoutingKey5);
        for (int i = 0; i < 8; i++) {
            //定义消息
            String message = "helloRabbit" + i;

            channel.basicPublish(directExchangeName,directRoutingKey3,null,message.getBytes());
            channel.basicPublish(directExchangeName,directRoutingKey4,null,message.getBytes());
            channel.basicPublish(directExchangeName,directRoutingKey5,null,message.getBytes());
        }
        //关闭通道
        channel.close();
        //关闭连接
        connection.close();

    }
}
