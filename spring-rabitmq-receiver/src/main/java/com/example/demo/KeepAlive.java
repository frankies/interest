/******************************************************************************/
/* SYSTEM     : YNA2.0                                                      */
/*                                                                            */
/*                                                           */
/******************************************************************************/
package com.example.demo;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * @author  YMSLX
 * @version 1.0
 *
 */
@Component
public class KeepAlive implements CommandLineRunner {


    /* (non-Javadoc)
     * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
     */
    @Override
    public void run(String... args) throws Exception {

        int i = 0;
        while(i++ < 10) {
            TimeUnit.SECONDS.sleep(100);
        }

    }
}
