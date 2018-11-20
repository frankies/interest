# Spring RabbitMQ Demo

 **-- 2018.11.20**

 转自 [springboot整合RabbitMQ](https://mp.weixin.qq.com/s/8UBJhrwQDSNNDLTZpdGzxQ)


## 目录结构

 - `spring-rabitmq-sender` 消息发送者
 - `spring-rabitmq-receiver` 消息接收者



 1. 消息发送： 

 - `SenderConf.java`  发送到队列中的普通消息（类型可以是String, 也可以一个可序列化的对象）

 - `SenderTopicConf.java` 将消息发送到可自定义主题的队列中

 - `SendFanoutExchangeConf.java` 将消息以广播的形式发送

 2. 消息接收

   - `ReceiverConf.java` 从队列中接收消息 
   - `ReceiverTopicConf.java` 接收到有主题的消息 
   - `ReceiverFanoutExchangeConf.java` 接收到广播消息 

## 启动步骤

  1. 先运行发送端的测试类： `MqSendApplicationTests.java`
  2. 再运行接收端的类： `MqReceiveApplicationTests.java`




