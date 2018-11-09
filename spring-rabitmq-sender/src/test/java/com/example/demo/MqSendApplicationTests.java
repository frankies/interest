package com.example.demo;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.SenderConf.Sender;
import com.example.demo.SenderTopicConf.TopicSender;
import com.example.demo.model.User;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
public class MqSendApplicationTests {

    @Autowired
    private Sender helloSender;

    @Autowired
    private TopicSender helloTopicSender;

    @Test
    public void testRabbit() throws InterruptedException {

        for (int i = 1; i <= 100; i++) {
            log.info("Sending direct {}", (i+1) );

            helloSender.send(String.format("%d. hello~~", i+1));
            helloSender.send(new User("Name-" + (i+1), i+1));
            TimeUnit.SECONDS.sleep(2);
        }
    }

    @Test
    public void testTopicRabbit() throws InterruptedException {

        for (int i = 1; i <= 100; i++) {
            log.info("Sending direct {}", (i+1) );
            helloTopicSender.send(String.format("%d. Topic message", i));
            TimeUnit.SECONDS.sleep(2);
        }
    }

}
