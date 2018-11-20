/******************************************************************************/
/* SYSTEM     : YNA2.0                                                      */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package com.example.demo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.SenderTopicConf.TopicSender;

import lombok.extern.slf4j.Slf4j;


/**
 *
 * @author  YMSLX
 * @version 1.0
 *
 */
@Slf4j
@Configuration
public class SendFanoutExchangeConf {

    private static final String FE_NAME = "fanoutExchange";
    @Bean(name="Amessage")
    public Queue AMessage() {
        return new Queue("fanout.A");
    }


    @Bean(name="Bmessage")
    public Queue BMessage() {
        return new Queue("fanout.B");
    }

    @Bean(name="Cmessage")
    public Queue CMessage() {
        return new Queue("fanout.C");
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(FE_NAME);//配置广播路由器
    }

    @Bean
    Binding bindingExchangeA(@Qualifier("Amessage") Queue aMessageQ,FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(aMessageQ).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeB(@Qualifier("Bmessage") Queue bMessageQ, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(bMessageQ).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeC(@Qualifier("Cmessage") Queue cMessageQ, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(cMessageQ).to(fanoutExchange);
    }

    @Bean
    public FanoutExchangeSender fanoutExchangeSender() {
         return new FanoutExchangeSender();
    }

    static class FanoutExchangeSender {

        @Autowired
        private AmqpTemplate template;


        public void send() {
             String msg = String.format("%s-%d", FE_NAME, System.currentTimeMillis());
            log.info("Send  '{}' with exechage '{}'", msg, FE_NAME);
            template.convertAndSend(FE_NAME, null, msg);
        }

    }
}
