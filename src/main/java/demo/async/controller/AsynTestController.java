package demo.async.controller;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import lombok.extern.slf4j.Slf4j;
 

/**
 * 
 * 
 * <a>https://mp.weixin.qq.com/s/Qd5thZbAmktr50MPcdt6Sg</a>
 * 
 * @version 1.0
 *
 */
@RestController
@RequestMapping("/async")
@Slf4j
public class AsynTestController {

    private static final Map<String, SseEmitter> EMITTER_MAP = new ConcurrentHashMap<>(1);

    /**
     * ResponseBodyEmitter 适用于要动态生成内容并逐步发送给客户端的场景
     * 
     * @return
     */
    @GetMapping("/bodyEmitter")
    public ResponseBodyEmitter handle() {
        // 创建一个ResponseBodyEmitter，-1代表不超时
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(-1L);
        // 异步执行耗时操作
        CompletableFuture.runAsync(() -> {
            try {
                // 模拟耗时操作
                for (int i = 0; i < 10000; i++) {
                    log.info("bodyEmitter " + i);
                    // 发送数据
                    emitter.send("bodyEmitter " + i + " @ " + LocalDateTime.now() + "\n");
                    Thread.sleep(2000);
                }
                // 完成
                emitter.complete();
            } catch (Exception e) {
                // 发生异常时结束接口
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    /**
     * <p>
     * SseEmitter 是 ResponseBodyEmitter 的一个子类，它同样能够实现动态内容生成，
     * 不过主要将它用在服务器向客户端推送实时数据， 如实时消息推送、状态更新等场景
     * 
     * </p>
     * <p>
     * SSE在服务器和客户端之间打开一个单向通道，服务端响应的不再是一次性的数据包而是text/event-stream类型的数据流信息，
     * 在有数据变更时从服务器流式传输到客户端。
     * </p>
     * <p>
     * 而且SSE有一点比较好，客户端与服务端一旦建立连接，即便服务端发生重启，也可以做到自动重连
     * </p>
     * 
     * @param userId
     * @return
     */
    @GetMapping("/subSseEmitter/{userId}")
    public SseEmitter sseEmitter(@PathVariable String userId) {
        log.info("sseEmitter: {}", userId);
        SseEmitter emitterTmp = new SseEmitter(-1L);
        EMITTER_MAP.put(userId, emitterTmp);
        CompletableFuture.runAsync(() -> {
            try {
                SseEmitter.SseEventBuilder event = SseEmitter.event()
                        .data("sseEmitter" + userId + " @ " + LocalTime.now()).id(String.valueOf(userId))
                        .name("sseEmitter");
                emitterTmp.send(event);
            } catch (Exception ex) {
                emitterTmp.completeWithError(ex);
            }
        });
        return emitterTmp;
    }

    @GetMapping("/sendSseMsg/{userId}")
    public void sseEmitter(@PathVariable String userId, String msg) throws IOException {
        SseEmitter sseEmitter = EMITTER_MAP.get(userId);
        if (sseEmitter == null) {
            return;
        }
        log.info("Send front message: " + msg);
        sseEmitter.send(msg);
    }

    /*
     * StreamingResponseBody
     * 与其他响应处理方式略有不同，主要用于处理大数据量或持续数据流的传输，支持将数据直接写入OutputStream
     * 例如，当我们需要下载一个超大文件时，使用 StreamingResponseBody 可以避免将文件数据一次性加载到内存中
     * 而是持续不断的把文件流发送给客户端，从而解决下载大文件时常见的内存溢出问题。
     * 
     * @return
     */
    @GetMapping("/streamingResponse")
    public ResponseEntity<StreamingResponseBody> handleRbe() {
        StreamingResponseBody stream = out -> {
            String message = "streamingResponse";
            for (int i = 0; i < 1000; i++) {
                try {
                    String omg = String.format("%s- %s\r\n",LocalDateTime.now(), (message + i));
                    out.write(omg.getBytes());
                    out.write("<br/>".getBytes());
                    // 调用一次flush就会像前端写入一次数据
                    out.flush();
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(stream);
    }

}

