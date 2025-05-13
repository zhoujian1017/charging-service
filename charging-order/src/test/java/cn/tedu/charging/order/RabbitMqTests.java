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

    //正确的测试用例，测试发布订阅模式
    @Test
    public void publishFanoutSuccess() throws Exception {
        //代码书写流程：


        //1. 创建连接工厂，创建连接，创建通道
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //2. 声明变量，定义routingKey
        String fanoutExchange = "hello_fanout_exchange";
        String routingKey = "hello_fanout_routing_key";
        String fanoutQueueName = "hello_fanout_queue";

        //定义交换机
        channel.exchangeDeclare(fanoutExchange, BuiltinExchangeType.FANOUT);

        //3.声明队列，通过routingKey将queue和exchange绑定
        for(int i=0;i<3;i++){
            //通过channel定义queue
            //fanout_queue_0  fanout_queue_1 fanout_queue_2
            String tempQueueName = fanoutQueueName + "_" + i;
            //fanout_routing_key_0 fanout_routing_key_1 fanout_routing_key_2
            String tempRoutingKey = routingKey + "_" + i;
            channel.queueDeclare(tempQueueName, false, false, false, null);
            channel.queueBind(tempQueueName, fanoutExchange, tempRoutingKey);
        }

        for (int i = 0; i < 6; i++) {
            //定义消息
            String message = "helloRabbit" + i;
            //发送消息
            //1 指定fanout类型的exchange，不指定routingKey，结果 和exchange绑定的queue,全部收到消息
            //channel.basicPublish(fanoutExchange,"",null,message.getBytes());
            //2 指定fanout类型的exchange，指定不属于当前Exchange的routingKey，结果和exchange绑定的queue,全部收到消息
            //channel.basicPublish(fanoutExchange,"direct_routing_key",null,message.getBytes());
            //3 指定fanout类型的exchange，指定属于当前Exchange的routingKey，结果 和exchange绑定的queue,全部收到消息
            //channel.basicPublish(fanoutExchange,"hello_fanout_routing_key_0",null,message.getBytes());
            //4 指定fanout类型的exchange，指定不存在routingKey，结果 和exchange绑定的queue,全部收到消息
            //String notExistRoutingKey = "aadfsadfsdf";
            //channel.basicPublish(fanoutExchange,notExistRoutingKey,null,message.getBytes());

            //总结 必须指定存在的 fanout类型 exchange, routingKey 不传,传对,传错, 全发 ,忽略 routingKey

            // 5 发送给不存在的 exchange  报异常 (reply-code=404, reply-text=NOT_FOUND - no exchange 'xxx' in vhost '/'
            //try {
            //    channel.basicPublish("xxx","xxx",null,message.getBytes());
            //}catch (Exception e) {
            //    e.printStackTrace();
            //}
            //6 发送 不指定 exchange 不指定  routingKey  消息丢失
            try {
                channel.basicPublish("","",null,message.getBytes());
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        //关闭通道
        channel.close();
        //关闭连接
        connection.close();

    }
}
