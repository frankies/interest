package com.quickstart.com.quickstart.s0001.coroute

import kotlinx.coroutines.delay
import java.time.LocalDateTime


suspend  fun getStringSlowly(): String {
    delay(1000)
    return "Got it! <-- ${LocalDateTime.now()} "
}


suspend fun main() {

    repeat(5) {
        println(getStringSlowly())
    }

    println("=== Try with printFiveTimes ===")
    printFiveTimes { // <<-- Like 'repeat' with inline modifier.
        getStringSlowly()
    }
}

inline fun printFiveTimes(generator: () -> String) { // `inline` 是关键点
    repeat(5) {
        println(generator())
    }
}
