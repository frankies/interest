package com.quickstart.s0001


data class Vect2(var x: Int, var y: Int) {
    operator fun plus(other: Vect2?) : Vect2 {
        val t = other ?: Vect2(0, 0)
        return Vect2(this.x + t.x, this.y + t.y)
    }
}

operator fun Vect2?.plus(other: Vect2?): Vect2 {
    return other + this
}

//operator fun Vect2?.plus(other: Vect2?): Vect2 {
//    val other = other ?: Vect2(0, 0)
//    val current = this ?: Vect2(0, 0)
//    return Vect2(current.x + other.x, current.y + other.y)
//}

fun main() {
    val v = Vect2(3, 9)
    val v1 = v + null
    val v2 = null + v
    val v3 = null + null

    println("$v, $v1, $v2, $v3")
}