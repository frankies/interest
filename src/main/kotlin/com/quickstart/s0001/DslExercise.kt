
package com.quickstart.s0001
fun html(build: StringBuilder.() -> Unit ) : String {
    val b = StringBuilder()
//    StringBuilder.apply(build)
    b.build()
    return b.toString()
}

fun StringBuilder.tag(name: String, content: String) {
    append(
        """
        <$name>$content</$name>
       """.trimIndent()
    )
}

fun StringBuilder.body(int: StringBuilder.() -> Unit) {
    append("<body>")
    int()
    append("</body>")
}

fun StringBuilder.h1(text: String) {
    tag("h1", text)
}

fun StringBuilder.p(text: String) {
    tag("p", text)
}

fun main() {
    val page = html {
        body {
            h1("Welcome to Kotlin")
            p("This page was built with a few lines of code.")
        }
    }
    println("HTML：\n" + page)
}