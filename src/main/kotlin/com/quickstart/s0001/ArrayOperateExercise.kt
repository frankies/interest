package com.quickstart.s0001

import kotlin.random.Random

val fruits = listOf("banana", "avocado", "apple", "kiwifruit")

class Booklet(val totalPages: Int) : Iterable<Int> {
    override fun iterator(): Iterator<Int> {
        return object : Iterator<Int> {
            var current = 1
            override fun hasNext() = current <= totalPages
            override fun next() = current++
        }
    }
}


fun main() {


    println("--- Loop ---")
    for (item in fruits) {
        println(item)
    }

    println("--- indices--")
    for(idx in fruits.indices) {
        println("$idx - ${fruits[idx]}")
    }

    println("--- index - value --")
    for((i, item) in fruits.withIndex()) {
        println("$i - $item")
    }

    println("--- while loop ---")
    var idx = 1
    while(idx  <= fruits.size ) {
        println("$idx - ${fruits[idx-1]}")
        idx++
    }

    //
    println("--- Iterable ---")
    val booklet = Booklet(3)
    for (page in booklet) {
        println("Reading page $page")
    }
    // Reading page 1
    // Reading page 2
    // Reading page 3

    //
    println("--- do while loop ---")
    var roll: Int
    do {
        roll = Random.nextInt(1, 7)
        println("Rolled a $roll")
    } while (roll != 6)
// Rolled a 2
// Rolled a 6

    println("Got a 6! Game over.")

    // Pass variable number of arguments to a function
    println("--- Pass variable number of arguments to a function\uFEFF ---")
    fun printAllStrings(vararg strings: String) {
        for (string in strings) {
            print(string)
        }
    }
    val lettersArray = arrayOf("c", "d")
    printAllStrings("a", "b", *lettersArray)

    // Compares contents of arrays
    println("--- Compares contents of arrays ---")
    val simpleArray = arrayOf(1, 2, 3)
    val anotherArray = arrayOf(1, 2, 3)

    println(simpleArray.contentEquals(anotherArray))


    // Using infix notation, compares contents of arrays after an element
    // is changed
        simpleArray[0] = 10
        println(simpleArray contentEquals anotherArray)
}