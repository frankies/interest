package com.example;

import org.apache.catalina.Context;
import org.junit.AfterClass;

import com.example.utils.TomcatTestSupport;

public abstract class BaseTomcatTest {

    protected static TomcatTestSupport.RunningTomcat server;
    private static boolean tearDownCalled = false;

    protected interface ServletConfigurer {
        void configure(Context ctx) throws Exception;
    }

    protected static void startTomcatServer(String contextPath, boolean enableJndi, ServletConfigurer configurer)
            throws Exception {
        server = TomcatTestSupport.start(contextPath, enableJndi, new TomcatTestSupport.ContextConfigurer() {
            public void configure(Context ctx) throws Exception {
                configurer.configure(ctx);
            }
        });
    }

    @AfterClass
    public static void tearDownServer() throws Exception {
        if (tearDownCalled) {
            return;
        }
        tearDownCalled = true;
        if (server == null) {
            return;
        }
        try {
            server.close();
        } catch (Exception ignored) {
        }
        server = null;
    }
}
