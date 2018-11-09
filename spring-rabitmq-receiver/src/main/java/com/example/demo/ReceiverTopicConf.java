/******************************************************************************/
/* SYSTEM     : YNA2.0                                                      */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package com.example.demo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author  YMSLX
 * @version 1.0
 *
 */
@Configuration
@Slf4j
public class ReceiverTopicConf {

    private final static String QMNAME = "topic.message";
    private final static String QMSNAME = "topic.messages";
    private final static String TOPIC_ONE = "topic.message";
    private final static String TOPIC_ALL = "topic.#";


    @Bean
    public Receiver sender() {
         return new Receiver();
    }


    static class Receiver {

        @RabbitListener(queues=QMNAME)    //监听器监听指定的Queue
        public void process1(String str) {
            log.info("message:"+str);
        }
        @RabbitListener(queues=QMSNAME)    //监听器监听指定的Queue
        public void process2(String str) {
            log.info("messages:"+str);
        }
    }
}
