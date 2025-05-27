import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class RestTest {

    public static void main(String[] args) {
        // 设置全局超时时间为3秒（3000毫秒）
        Unirest.config()
                .socketTimeout(3000)
                .connectTimeout(3000);

        int requestCount = 1000;
        Thread[] threads = new Thread[requestCount];
        long delay = 50L;

        for (int i = 0; i < requestCount; i++) {
            final int idx = i;
            threads[i] = Thread.ofVirtual().start(() -> {
                final var tid = idx + 1;
                try {
                    // 平滑发出请求，每个线程启动间隔100毫秒
                    Thread.sleep(idx * delay);
                    HttpResponse<String> response = Unirest.get("https://elearning.ylearn.co.id/public/test/test.json?currentPage=1&pageSize=3").asString();
                    if (response.getStatus() == 200) {
                        System.out.println("%d - ✅ OK".formatted(tid));
                    } else {
                        System.out.println("%d - ❌ NG".formatted(tid));
                    }
                } catch (Exception e) {
                    System.out.println("%d - ❌NG - %s".formatted(tid, e.getMessage()));
                }
            });
        }

        // 使用 join 等待所有虚拟线程结束
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}