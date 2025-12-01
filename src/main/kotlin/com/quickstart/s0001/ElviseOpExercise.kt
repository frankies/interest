package com.quickstart.s0001



fun getValueFromApi() :String? {
    return null
}

fun main() {
    val value = getValueFromApi() ?: return // <-- return to skip following execution. And the type is not nullable.
//     val value = getValueFromApi() ?: error("value is null")
    val uc = value.uppercase()
    println(uc)

}