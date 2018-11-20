
package com.example.demo;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author  YMSLX
 * @version 1.0
 *
 */
@Slf4j
public enum AnsiColorLogger {


     ANSI_BLACK("\u001B[30m"),
     ANSI_RED("\u001B[31m"),
     ANSI_GREEN("\u001B[32m"),
     ANSI_YELLOW("\u001B[33m"),
     ANSI_BLUE("\u001B[34m"),
     ANSI_PURPLE("\u001B[35m"),
     ANSI_CYAN("\u001B[36m"),
     ANSI_WHITE("\u001B[37m"),

     ANSI_RESET("\u001B[0m");


    private String color;

    /**
     *
     */
    private AnsiColorLogger(String color) {
        this.color = color;
    }

    public  void log(String text, Object... args) {
        log.info( this.color + text + ANSI_RESET.color, args);
    }
}
