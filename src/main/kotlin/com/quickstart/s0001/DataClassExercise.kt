package com.quickstart.s0001

import kotlin.math.exp

data class Person2(val name: String, val age: Int)

fun getPeople(): List<Person2> {
    return listOf(Person2("Alice", 29), Person2("Bob", 31))
}

fun comparePeople(): Boolean {
    val p1 = Person2("Alice", 29)
    val p2 = Person2("Alice", 29)
    return p1 == p2  // should be true
}

////


fun eval(Expr2: Expr2): Int =
    when (Expr2) {
        is Num2 -> Expr2.value
        is Sum2 -> eval(Expr2.left) + eval(Expr2.right)
        else -> throw IllegalArgumentException("Unknown Expr2ession")
    }
interface Expr2
class Num2(val value: Int) : Expr2
class Sum2(val left: Expr2, val right: Expr2) : Expr2


fun main() {
    val people = getPeople()
    println(people)

    val comparePeople = comparePeople()
    println(comparePeople)
}