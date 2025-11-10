package com.quickstart.s0001

import java.math.BigDecimal

class Sample {

    fun sum(a: Int, b: Int) = a + b
}

fun main() {
    var a = 1
    var b = 2
    a = b.also { b = a ; println(it) } // 先把 b赋值给a, 再执行 b=a
    println("$a, $b")

    //
    fun calcTaxes(): BigDecimal = TODO("Waiting for feedback from accounting")

}