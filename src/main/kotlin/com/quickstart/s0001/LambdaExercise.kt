package com.quickstart.s0001

enum class Color(r: Int, g: Int, b: Int) {
    White(256, 256, 256)
}

/**
 *  Calculate number with odd or even flag into list
 */
fun task(): List<Boolean> {
    val isEven: Int.() -> Boolean = { this % 2 == 0 }
    val isOdd: Int.() -> Boolean = { this % 2 != 0 }
    return listOf(42.isOdd(), 239.isOdd(), 294823098.isEven())
}

/**
 *  Use sum operation
 */
val sum = fun Int.(other: Int): Int = this + other


fun main() {
    println(task())

    val v1 = 2
    val v2 = 3
    val v3 = 4
    println((v2.sum(v1).sum(v3)))
}