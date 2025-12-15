package com.quickstart.s0001.corouting

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

// Quick start with corouting in Kotlin
// Modeling Rusty’s project by calling the order() and perform() functions.
// @see https://typealias.com/start/kotlin-coroutines/


/**
 *
 *  Single-Threaded, Blocking Code
 *
 */
fun singleConstruction() {
    val windows = order(Product.WINDOWS)
    val doors = order(Product.DOORS)
    perform("🧱laying bricks")
    perform("installing ${windows.description}")
    perform("installing ${doors.description}")
}

fun coroutingV10() {
    runBlocking {
        val windows = supOrder(Product.WINDOWS)
        val doors = supOrder(Product.DOORS)
        perform("🧱laying bricks")
        perform("installing ${windows.description}")
        perform("installing ${doors.description}")
    }
}


fun coroutingV20() {
    runBlocking {
        val windows = async { supOrder(Product.WINDOWS) }
        val doors = async { supOrder(Product.DOORS) }
        launch {
            perform("🧱laying bricks")
            perform("install ${windows.await().description}")
            perform("install ${doors.await().description}")
        }
    }
}

fun coroutingV30() {
    runBlocking {
        val windows = async(Dispatchers.IO) { supOrder(Product.WINDOWS) }
        val doors = async { supOrder(Product.DOORS) }
        launch(Dispatchers.Default)  {
            perform("🧱laying bricks")
            perform("install ${windows.await().description}")
            perform("install ${doors.await().description}")
        }
    }
}

fun coroutingV40() {
    runBlocking {
        val windows = async(Dispatchers.IO) { supOrder(Product.WINDOWS) }
        val doors = async { supOrder(Product.DOORS) }
        launch(Dispatchers.Default)  {
            perform("🧱laying bricks")
            launch { perform("install ${windows.await().description}") }
            launch { perform("install ${doors.await().description}") }
        }
    }
}

/**
 * ❌ The windows and doors are supposed to be installed only after the bricks have been laid.
 */
fun coroutingV50() {
    runBlocking {
        launch(Dispatchers.IO) {
            val windows = order(Product.WINDOWS)
            withContext(Dispatchers.Default) {
                perform("install ${windows.description}")
            }
        }
        launch(Dispatchers.IO) {
            val doors = order(Product.DOORS)
            withContext(Dispatchers.Default) {
                perform("install ${doors.description}")
            }
        }
        launch(Dispatchers.Default) {
            perform("laying bricks")
        }
    }
}

fun coroutingV60() {
    runBlocking {
        val bricksJob = launch(Dispatchers.Default) {
            perform("laying bricks")
        }
        launch(Dispatchers.IO) {
            val windows = order(Product.WINDOWS)
            bricksJob.join()
            withContext(Dispatchers.Default) {
                perform("install ${windows.description}")
            }
        }
        launch(Dispatchers.IO) {
            val doors = order(Product.DOORS)
            bricksJob.join()
            withContext(Dispatchers.Default) {
                perform("install ${doors.description}")
            }
        }
    }
}

fun coroutingV61() {
    runBlocking {
        val bricksJob = launch(Dispatchers.Default) {
            suspPerform("laying bricks")
        }
        launch(Dispatchers.IO) {
            val windows = order(Product.WINDOWS)
            bricksJob.join()
            withContext(Dispatchers.Default) {
                perform("install ${windows.description}")
            }
        }
        launch(Dispatchers.IO) {
            val doors = order(Product.DOORS)
            bricksJob.join()

            withContext(Dispatchers.Default) {
                perform("install ${doors.description}")
            }
        }
        cancel()
    }
}

/**
 * Everything else continued according to plan - the bricks were laid,
 * and the windows were installed. So, when we cancel a coroutine, that coroutine itself is canceled,
 * and if it has any children,
 * they are also canceled. But parent and sibling coroutines are unaffected.
 */
fun coroutingV62() {
    runBlocking {
        val bricksJob = launch(Dispatchers.Default) {
            suspPerform("laying bricks")
        }
        launch(Dispatchers.IO) {
            val windows = order(Product.WINDOWS)
            bricksJob.join()
            withContext(Dispatchers.Default) {
                perform("install ${windows.description}")
            }
        }
        launch(Dispatchers.IO) {
            val doors = order(Product.DOORS)
            bricksJob.join()

            cancel()
            withContext(Dispatchers.Default) {
                perform("install ${doors.description}")
            }
        }

    }
}

fun coroutingV63WithError() {
    runBlocking {
        val bricksJob = launch(Dispatchers.Default) {
            perform("laying bricks")
        }
        launch(Dispatchers.IO) {
            val windows = order(Product.WINDOWS)
            bricksJob.join()
            withContext(Dispatchers.Default) { perform("install ${windows.description}") }
        }
        launch(Dispatchers.IO) {
            val doors = order(Product.DOORS)
            bricksJob.join()
            throw Exception("Out of money!")
//            withContext(Dispatchers.Default) { perform("install ${doors.description}") }
        }
    }
}




fun main() {

    // Single-Threaded, Blocking Code
//    singleConstruction()

    //
//    coroutingV10()

    //
//    coroutingV20()


    //
//    coroutingV30()

    //
//    coroutingV40()


    //
//    coroutingV50()

//    coroutingV60()

    //
//    coroutingV61()


    //

//    coroutingV62()

    //
    coroutingV63WithError()
    //
//    val job = launch { perform("laying bricks") }
//    job.cancel()
}





enum class Product(val description: String, val deliveryTime: Long) {
    DOORS("🥅doors", 750),
    WINDOWS("🪟windows", 1_250)
}

suspend fun supOrder(item: Product): Product {
    println("🎬ORDER EN ROUTE  >>> The ${item.description} are on the way!")
    delay(item.deliveryTime)
    println("🎯ORDER DELIVERED >>> Your ${item.description} have arrived.")
    return item
}

fun order(item: Product): Product {
    println("🎬ORDER EN ROUTE  >>> The ${item.description} are on the way!")
    Thread.sleep(item.deliveryTime)
    println("🎯ORDER DELIVERED >>> Your ${item.description} have arrived.")
    return item
}

fun perform(taskName: String) {
    println("⛏️STARTING TASK   >>> $taskName")
    Thread.sleep(1_000)
    println("✅FINISHED TASK   >>> $taskName")
}

suspend fun suspPerform(taskName: String) {
    println("STARTING TASK   >>> $taskName")
    repeat(5) {
        Thread.sleep(200)
        yield()
    }
    println("FINISHED TASK   >>> $taskName")
}