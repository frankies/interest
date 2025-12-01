package com.quickstart.s0001

// data 修饰符时，对象就已经有 component1 和 component2 方法，
// 也就自然就支持解构操作
data class User(val name: String, val addr: String, val age: Int) //<-- data is key

// 普通一个类就需要实现  component1 和 component2 方法
class Vect2(val x: Int, val y: Int) {
    operator fun component1() = x
    operator fun component2() = y
}

fun main() {

    val g = Pair("Gold", 1)
    println("${g.second} -> ${g.first}")


    val c = "Silver" to 2
    val (metal, pos) = c
    println("${pos} -> ${metal}")

    /// List
    val basket = listOf("Banana", "Apple", "Pear")
    val (first, second, third) = basket
    println("1->$first, 2->$second, 3->$third")

    /// Map
    val medals = mapOf(
        1 to "Gold",
        2 to "Silver",
        3 to "Bronze"
    )
    for((pos, metal) in medals) {
     println("Map: $pos -> $metal")
    }

    for(value in medals.values) {
        println("Value:  $value")
    }

    ///
    val jhon = User("Jhon Smith", "ss", 22)
    val (name, addr) = jhon
    println("$name, $addr")

    //
    val (x, y) = Vect2(x = 1, y = 2)
    println("Vecotr: $x, $y")
}