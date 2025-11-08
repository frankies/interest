package com.quickstart.com.quickstart.s0001

fun buildString(build: StringBuilder.() -> Unit): String {
    println("Begin fun -> buildString")
    val stringBuilder = StringBuilder()
    stringBuilder.build()
    println("End fun -> buildString")
    return stringBuilder.toString()
}

//
fun <K, V> buildMutableMap(build: HashMap<K, V>.() -> Unit): Map<K, V> {
    val m  = HashMap<K, V>()
    m.build()
    return m;
}
fun usage(): Map<Int, String> {
    return buildMutableMap {
        put(0, "0")
        for (i in 1..10) {
            put(i, "$i")
        }
    }
}


fun main() {
    val s = buildString {
        println("Entry -> buildString")
        append("Numbers: ")
        for (i in 1..3) {
            // 'this' can be omitted
            append(i)
        }
        println("Exit -> buildString")
    }
    val expected = "Numbers: 123"
    println( "Is it expected: $expected? -> ${s == expected}")

    // Use map builder
    val m = usage()
    assert(m.keys.size == 10)
    println(m)
}