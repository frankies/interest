package com.quickstart.s0001.corouting

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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




fun main() {

    // Single-Threaded, Blocking Code
//    singleConstruction()

}
enum class Product(val description: String, val deliveryTime: Long) {
    DOORS("🥅doors", 750),
    WINDOWS("🪟windows", 1_250)
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