
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
 
@Slf4j
public class WebSocketClientDemo {

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(); // executor for scheduled task
    private List<CharSequence> textParts = new ArrayList<>(16); //cache text data
    private List<ByteBuffer> binaryParts = new ArrayList<>(16); //cache binary data
    private String url; 
    private WebSocket webSocket;

    public WebSocketClientDemo(String url, String token) {
        this.url = url;
        var wsCompletableFuture =
                HttpClient.newHttpClient().newWebSocketBuilder()
                .header("x-auth-token", token)
                .connectTimeout(Duration.ofSeconds(1))
                .buildAsync(URI.create(url), new WebSocketListener());
        
        
        webSocket = wsCompletableFuture.join();
        executor.scheduleAtFixedRate(()->{
            // send ping msg  to keep alive every 5 second
            if (webSocket!=null && !webSocket.isOutputClosed()) {
                log.info("send ping");
                var objectMapper = new ObjectMapper(); //jackson
                var map = new HashMap<String,Object>(1);
                map.put("ping",System.currentTimeMillis());
                try {
                    webSocket.sendText(objectMapper.writeValueAsString(map),true);
                } catch (JsonProcessingException ignore) {
                }
            }
        },5,5,TimeUnit.SECONDS);
    }

    private class WebSocketListener implements WebSocket.Listener {

        @Override
        public void onOpen(WebSocket webSocket) {
            log.info("onOpen: websocket opened.");
            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            webSocket.request(1);
            textParts.add(data);
            if (last) {
                String content = String.join("", textParts);
                log.info("onText: {}", content);
                textParts.clear();
            }
            return null;
        }
        @Override
        public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
            webSocket.request(1);
            binaryParts.add(data);
            if (last) {
                int size = 0; 
                for (var binaryPart : binaryParts) {
                    size += binaryPart.array().length;
                }
                var allocate = ByteBuffer.allocate(size);
                binaryParts.forEach(allocate::put);
                binaryParts.clear();
                var content = uncompress(allocate.array());
                log.info("onBinary: {}", new String(content));
            }
            return null;
        }
        @Override
        public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
            webSocket.request(1);
            log.info("onPing: {}",  message.asCharBuffer().toString());
            return null;
        }
        @Override
        public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
            webSocket.request(1);
            log.info("onPong: {}", "pong");
            return null;
        }
        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            log.info("ws closed with status({}). cause:{}",  statusCode , reason);
            webSocket.sendClose(statusCode, reason);
            return null;
        }
        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            log.info("error: {}", error.getLocalizedMessage());
            webSocket.abort();
        }

    }

    private byte[] uncompress(byte[] bytes) {
      // ...
        return bytes;
    }

    public String getUrl() {
        return url;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

     public static void main(String[] args) throws JsonProcessingException, InterruptedException {
        var huobiWebSocket = new WebSocketClientDemo(
                                                      "ws://localhost:8080/blank/websocket/common/user", //指定URL
                                                      "760a10f5-bd33-4589-93c8-54c3355d8778" // 添加头部的 x-auth-token 的值
                                                     );
        var map = new HashMap<String, Object>();
        map.put("sub","market.btcusdt.kline.1min");
        var objectMapper = new ObjectMapper();
        huobiWebSocket.getWebSocket().sendText(objectMapper.writeValueAsString(map),true);
        

//        TimeUnit.SECONDS.sleep(10);
//        huobiWebSocket.getWebSocket().sendClose(WebSocket.NORMAL_CLOSURE, "ok").join();
    }
}