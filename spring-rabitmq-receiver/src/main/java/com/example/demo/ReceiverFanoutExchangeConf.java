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
public class ReceiverFanoutExchangeConf {


    @Bean
    public Receiver fanoutExchangeReceiver() {
         return new Receiver();
    }


    static class Receiver {

        @RabbitListener(queues="fanout.A")
        public void processA(String str) {
            log.info("ReceiveA:"+ str);
        }
        @RabbitListener(queues="fanout.B")
        public void processB(String str) {
            log.info("ReceiveB:"+ str);
        }
        @RabbitListener(queues="fanout.C")
        public void processC(String str) {
            log.info("ReceiveC:"+ str);
        }
    }
}
