package com.quickstart.com.quickstart.s0001

import java.net.URI
import kotlin.random.Random

/**
Here is a short guide for choosing scope functions depending on the intended purpose:

Executing a lambda on non-nullable objects: let

Introducing an expression as a variable in local scope: let

Object configuration: apply

Object configuration and computing the result: run

Running statements where an expression is required: non-extension run

Additional effects: also

Grouping function calls on an object: with

 -----

Scope functions differ by the result they return:
apply and also return the context object.
let, run, and with return the lambda result.
**/

fun writeToLog(message: String) {
    println("INFO: $message")
}

data class Person(val name: String,var age: Int = -1,var location: String = "") {
    fun moveTo(newLocation: String) {
        location = newLocation
    }
    fun incrementAge() {
       age += 1
    }
}

fun letTest() {
    Person("Alice", 20, "Amsterdam").let {
        println(it)
        it.moveTo("London")
        it.incrementAge()
        println(it)
    }
}

fun main() {
    letTest()

    println("------------")
    val str = "Hello"
    // run -> this
    str.run {
        println("The string's length: $length")
        //println("The string's length: ${this.length}") // does the same
    }

    // let -> it
    str.let {
        println("The string's length is ${it.length}")
    }

    val adam = Person("Adam").apply {
        age = 20                       // same as this.age = 20
        location = "London"
    }
    println(adam)

    // also -> it
    fun getRandomInt(): Int {
        return Random.nextInt(100).also {
            println("getRandomInt() generated value $it")
            // return content object by default
        }
    }

    fun getRandomInt2(): Int {
        return Random.nextInt(100).also { value ->
           writeToLog("getRandomInt2() generated value $value")

        }
    }
    val i = getRandomInt()
    println(i)

    val j = getRandomInt2()
    println(j)

    // Context object
    val numberList = mutableListOf<Double>()
    numberList.also { println("Populating the list") }
        .also { println("Before, list size: ${it.size}") }
        .apply {
            add(2.71)
            add(3.14)
            add(1.0)
        }
        .also { println("After, list size: ${it.size}") }
        .also { println("Sorting the list") }
        .sort()
    println(numberList)

    // .run -> Context Object this
    val numbers = mutableListOf("one", "two", "three")
    val countEndsWithE = numbers.run {
        also { println("Before ending with element: $it") }
        add("four")
        add("five")
        also { println("After ending with element: $it") }
        count { it.endsWith("e") }
    }

    // with -> Context Object this
    val numbers2 = mutableListOf("one", "two", "three")
    with(numbers2) {
        val firstItem = first()
        val lastItem = last()
        println("First item: $firstItem, last item: $lastItem")
    }
    println("There are $countEndsWithE elements that end with e.")

    //
    val numbers3 = mutableListOf("one", "two", "three", "four", "five")
    val resultList = numbers3.map { it.length }.filter { it > 3 }
    println(resultList)
}


