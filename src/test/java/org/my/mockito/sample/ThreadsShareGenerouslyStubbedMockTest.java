/******************************************************************************/
/* SYSTEM     :                                                       */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package org.my.mockito.sample;

import org.junit.Test;
import org.my.mockito.sample.model.IMethods;

import static org.mockito.Mockito.*;
/**
 *
 * @author  
 * @version 1.0
 *
 */
public class ThreadsShareGenerouslyStubbedMockTest  {

    private IMethods mock;

    @Test
    public void shouldAllowVerifyingInThreads() throws Exception {
        for(int i = 0; i < 50; i++) {
            performTest();
        }
    }

    private void performTest() throws InterruptedException {
        mock = mock(IMethods.class);

        when(mock.simpleMethod("foo"))
            .thenReturn("foo")
            .thenReturn("bar")
            .thenReturn("baz")
            .thenReturn("foo")
            .thenReturn("bar")
            .thenReturn("baz");


        final Thread[] listeners = new Thread[1];
        for (int i = 0; i < listeners.length; i++) {

            listeners[i] = new Thread() {
                @Override
                public void run() {
                    try {
                        for (int j = 1; j <= 6; j++) {
                            System.out.printf("Thread %s, %d.result:%s\n", Thread.currentThread().getName(), j,  mock.simpleMethod("foo"));
                        }

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            listeners[i].start();
        }
        for (Thread listener : listeners) {
            listener.join();
        }
    }
}