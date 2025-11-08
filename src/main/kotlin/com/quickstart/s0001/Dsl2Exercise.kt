package com.quickstart.com.quickstart.s0001

interface HtmlTag {
    fun innerHTML(tag: String): String = "<$tag></$tag>"
}

data class Text(val text: String) : HtmlTag {
}
data class ScriptBlock(var content: Int) {}
class HeaderScriptLink(val id: String?, val url: String)
class HeaderStyleLink(val id: String?, val name: String)

class Header()


class HTML(val headerHtml: StringBuilder = StringBuilder(), val bodyHtml: StringBuilder = StringBuilder()) {
    fun body(content: String): Unit {
        with(bodyHtml) {
            append("<body>")
            append("$content")
            append("</body>")
        }
    }
    fun html(): String {
        return """
            <html>
             ${bodyHtml}
            </html>
        """.trimIndent()
    }
}

fun html(init: HTML.() -> Unit): HTML {
    val html = HTML()
     html.init()
    return html
}

fun main() {
    val html = html {
      body("Welcome to Kotlin")
    }
    println(html.html())
}