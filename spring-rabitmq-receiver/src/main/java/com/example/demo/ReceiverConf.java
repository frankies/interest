/******************************************************************************/
/* SYSTEM     : YNA2.0                                                      */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package com.example.demo;

import java.io.Serializable;

import org.springframework.amqp.core.Queue;
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
public class ReceiverConf {

    private final static String QNAME = "queue";

    @Bean
    public Queue queue() {
         return new Queue(QNAME);
    }

    @Bean
    public Receiver receiver() {
         return new Receiver();
    }


    static class Receiver {

        @RabbitListener(queues = QNAME)    //监听器监听指定的Queue
        public void processC(Serializable str) {
//            new RuntimeException().printStackTrace();
            log.info("Receive {}:"+str, QNAME);

        }
    }
}
