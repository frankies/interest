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
    val state: String
      get() = if (age > 30) "old man" else "young man"
}

fun letTest() {
    Person("Alice", 20, "Amsterdam").let {
        val oldLocation = it.location
        it.moveTo("London")
        it.incrementAge()
        println("${it.name} has move from ${oldLocation} to ${it.location}")
        println("State: " + it.state)
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
    //    上下文对象作为 lambda 表达式的参数（it）来访问。
    //    返回值是 lambda 表达式的结果。
    val sl = str.let {
        println("The string's length is ${it.length}")
        str.length
    }
    println("Return $sl")

    // apply -> this
    //    上下文对象 作为接收者（this）来访问。
    //    返回值 是上下文对象本身。
    val adam = Person("Adam").apply {
        age = 20                       // same as this.age = 20
        location = "London"
    }
    println(adam)

    // also -> it
    //    上下文对象作为 lambda 表达式的参数（it）来访问。
    //    返回值是上下文对象本身。
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
    //    let、run 及 with 返回 lambda 表达式的结果。
    //    所以，在需要使用其结果给一个变量赋值，或者在需要对其结果进行链式操作等情况下，可以使用它们。
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

    // let -> it
    val numbers3 = mutableListOf("one", "two", "three", "four", "five")
     numbers3.map { it.length }.filter { it > 3 }.let {
        println("Length >3: $it")
    }
    numbers3.map { it.length }.filter { it > 3 }.let(::println)

    // let: Usage in non-null case
    fun processNonNullString(str: String) { println("Processing string: $str") }
    val str2: String? = "Hello"
    //processNonNullString(str)       // 编译错误：str 可能为空
    val length = str?.let {
        println("let() called on $it")
        processNonNullString(it)      // 编译通过：'it' 在 '?.let { }' 中必不为空
        it.length // 返回结果
    }

    // Pipeline process
    val numbers4 = listOf("one", "two", "three", "four")
    val modifiedFirstItem = numbers4.first().let { firstItem ->
        println("The first item of the list is '$firstItem'")
        if (firstItem.length >= 5) firstItem else "!" + firstItem + "!"
    }.uppercase()
    println("First item after modifications: '$modifiedFirstItem'")

    /*
      apply 及 also 返回上下文对象。
      let、run 及 with 返回 lambda 表达式结果.
    */
    val numberList5 = mutableListOf<Double>()
    numberList5.also { println("Populating the list") }
        .apply {
            add(2.71)
            add(3.14)
            add(1.0)
        }
        .also { println("Sorting the list") }
        .sort()
    println(numberList5)

    // with -> this
    // 上下文对象作为接收者（this）使用。
    // 返回值是 lambda 表达式结果。

    val numbers7 = mutableListOf("one", "two", "three")
    with(numbers7) {
        println("'with' is called with argument $this")
        println("It contains $size elements")
    }

    val firstAndLast = with(numbers) {
        "The first element is ${first()}," +
                " the last element is ${last()}"
    }
    println("firstAndLast: $firstAndLast")

    // run -> this
    // 上下文对象 作为接收者（this）来访问。
    // 返回值 是 lambda 表达式结果。
    class MultiportService(var url: String, var port: Int) {
        fun prepareRequest(): String = "Default request"
        fun query(request: String): String = "Result for query '$request'"
    }

    val service = MultiportService("https://example.kotlinlang.org", 80)
    val result = service.run {
        port = 8080
        query(prepareRequest() + " to port $port")
    }
    // 同样的代码如果用 let() 函数来写:
    val letResult = service.let {
        it.port = 8080
        it.query(it.prepareRequest() + " to port ${it.port}")
    }
    println(result)
    println(letResult)

    //    还可以将 run 作为非扩展函数调用。run 的非扩展变体没有上下文对象，但它仍然返回 lambda 结果。
    //    非扩展 run 可以在需要表达式的地方执行一个由多个语句组成的块。在代码中，非扩展运行可以理解为“ 运行代码块并计算结果”
    val hexNumberRegex = run {
        val digits = "0-9"
        val hexDigits = "A-Fa-f"
        val sign = "+-"

        Regex("[$sign]?[$digits$hexDigits]+")
    }
    println("Matching regex: $hexNumberRegex")
    for (match in hexNumberRegex.findAll("+123 -FFFF !%*& 88 XYZ")) {
        println(match.value)
    }

    // takeIf 与 takeUnless
    val number = Random.nextInt(100)

    val evenOrNull = number.takeIf { it % 2 == 0 }
    val oddOrNull = number.takeUnless { it % 2 == 0 }
    println("even: $evenOrNull, odd: $oddOrNull")

    val strs = listOf("", "two")
    strs.forEach { s ->
        val caps = s.takeIf { it.isNotEmpty() }?.uppercase()
        println("Caps: -> $caps")
    }

    fun displaySubstringPosition(input: String, sub: String) {
        input.indexOf(sub).takeIf { it >= 0 }?.let {
            println("The substring $sub is found in $input.")
            println("Its start position is $it.")
        }
    }
    displaySubstringPosition("010000011", "11")
    displaySubstringPosition("010000011", "12")

    val v: List<String?> = listOf("one", "two", null)
    v.filter { !it.isNullOrEmpty() }.forEach(::print)
}


