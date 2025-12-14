package com.quickstart.s0001
 

data class Vect(var x: Int, var y: Int) {
    operator fun plus(other: Vect?) : Vect {
        val t = other ?: Vect(0, 0)
        return Vect(this.x + t.x, this.y + t.y)
    }
}

operator fun Vect?.plus(other: Vect?): Vect {
    return other + this
}

//operator fun Vect?.plus(other: Vect?): Vect {
//    val other = other ?: Vect(0, 0)
//    val current = this ?: Vect(0, 0)
//    return Vect(current.x + other.x, current.y + other.y)
//}

fun main() {
    val v = Vect(3, 9)
    val v1 = v + null
    val v2 = null + v
    val v3 = null + null

    println("$v, $v1, $v2, $v3")
}