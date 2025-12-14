package com.quickstart.s0001.corouting

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield


fun coroutingV1() {
    runBlocking {
        println("Sledge: Suplex!")
        println("Hammer: Clothesline!")
        println("Sledge: Figure-four Leglock!")
        println("Hammer: Piledriver!")
        println("Sledge: Pinning 1-2-3!")
    }
}

/**
 * ❌
 * Wrong order
 */
fun coroutingV2() {
    runBlocking {
        launch {
            println("Hammer: Clothesline!")
            println("Hammer: Piledriver!")
        }
        println("Sledge: Suplex!")
        println("Sledge: Figure-four Leglock!")
        println("Sledge: Pinning 1-2-3!")
    }
}

/**
 * ✅
 * yield() is a suspending function, and each time we call it, the coroutine hits a suspension point.
 * This yields the wrestling ring back to the wrestler’s teammate - in other words,
 * it gives the other coroutine a chance to run some of its code.
 * So, the execution path bounces back and forth between the runBlocking() lambda and the launch() lambda,
 * producing the following output, which shows the wrestling moves in the correct order.
 *
 */
fun coroutingV3() {
    runBlocking {
        launch {
            println("Hammer: Clothesline!")
            yield()

            println("Hammer: Piledriver!")
            yield()
        }
        println("Sledge: Suplex!")
        yield()
        println("Sledge: Figure-four Leglock!")
        yield()
        println("Sledge: Pinning 1-2-3!")
    }
}


fun coroutingV4() {
    runBlocking {
        launch {
            println("Hammer: Clothesline!")
            tagOut()

            println("Hammer: Piledriver!")
            tagOut()
        }
        println("Sledge: Suplex!")
        tagOut()
        println("Sledge: Figure-four Leglock!")
        tagOut()
        println("Sledge: Pinning 1-2-3!")
    }
}

suspend fun tagOut() {
    println("    🧑🏿‍🦽‍➡️Tagging out!    ")
    yield()
}

fun main() {

    // Corouting v1
    coroutingV1()

    // Corouting v2 - with runBlocking
    coroutingV2()

    // coroutingV3 - with yield
    coroutingV3()

    // coroutingV4 - with tagout "suspend fun"
    coroutingV4()
}