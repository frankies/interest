package com.quickstart.s0001.ext


/**
 *
 */
fun Int.r(): RationalNumber = RationalNumber(this, 1)

fun Pair<Int, Int>.r(): RationalNumber = RationalNumber(first, second)

/**
 *  有理数： 分子 / 分母
 */
data class RationalNumber(val numerator: Int, val denominator: Int) {
    override fun toString(): String {
        return "$numerator/$denominator"
    }
}


data class Pair<out A, out B>(
    val first: A,
    val second: B
)

fun main() {
  val xx = listOf(
      Pair(1, 3),
      2,
      Pair(3, 2)
  )
 
}