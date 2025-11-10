package com.quickstart.s0001

fun stringLength(a: String?): Int = a?.length ?: 0

fun main() {
    val content = "Hell2345dfasdfasdfasdfo"
    println("""Length of word '${content}' is ${stringLength(content)}""")

}