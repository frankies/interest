/******************************************************************************/
/* SYSTEM     : YNA2.0                                                      */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package com.example.demo;

import org.springframework.amqp.core.Queue;

import java.io.Serializable;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author  YMSLX
 * @version 1.0
 *
 */
@Configuration
public class SenderConf {

    private final static String QNAME = "queue";

    @Bean
    public Queue queue() {
         return new Queue(QNAME);
    }

    @Bean
    public Sender sender() {
         return new Sender();
    }


    static class Sender {

        @Autowired
        private AmqpTemplate template;

        public void send(Serializable msg) {
            template.convertAndSend(QNAME,  msg);
        }


    }
}
