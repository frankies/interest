/******************************************************************************/
/* SYSTEM     : YNA2.0                                                      */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package com.example.demo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.SenderConf.Sender;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author  YMSLX
 * @version 1.0
 *
 */
@Configuration
@Slf4j
public class SenderTopicConf {

    private final static String QMNAME = "topic.message";
    private final static String QMSNAME = "topic.messages";
    private final static String TOPIC_ONE = "topic.message";
    private final static String TOPIC_ALL = "topic.#";
    private final static String EXCHAGE = "exchange";

    @Bean(name="message")
    public Queue queueMessage() {
        return new Queue(QMNAME);
    }

    @Bean(name="messages")
    public Queue queueMessages() {
        return new Queue(QMSNAME);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHAGE);
    }

    @Bean
    Binding bindingExchangeMessage( TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage()).to(exchange).with(TOPIC_ONE);
    }

    @Bean
    Binding bindingExchangeMessages( TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages()).to(exchange).with(TOPIC_ALL);//*表示一个词,#表示零个或多个词
    }

    @Bean
    public TopicSender topicSender() {
         return new TopicSender();
    }

    static class TopicSender {

        @Autowired
        private AmqpTemplate template;

        private String[] routeKeys = new String[] {"topic.%d", TOPIC_ONE};

        public void send(String msg) {
            double j = Math.random() * (routeKeys.length-1);
             int idx = Long.valueOf(Math.round(j)).intValue();
             String rk = String.format(routeKeys[idx], System.currentTimeMillis());
            log.info("Send '{}' with routkey '{}'", msg, rk);
            template.convertAndSend(EXCHAGE, rk, msg);
        }

    }
}
