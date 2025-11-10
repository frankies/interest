package com.quickstart.s0001

open class Tag(val name: String) {
    protected val children = mutableListOf<Tag>()

    override fun toString() =
        "<$name>${children.joinToString("")}</$name>"
}

fun table(init: TABLE.() -> Unit): TABLE {
    val table = TABLE()
    table.init()
    return table
}

class TABLE : Tag("table") {
    fun tr(init: TR.() -> Unit) {
        children.add(TR().apply(init))
    }
}

class TR : Tag("tr") {
    fun td(init: TD.() -> Unit) {
        children.add(TD().apply(init))
    }
}

class TD : Tag("td")

fun createTable() =
    table {
        tr {
            repeat(2) {
                td {
                }
            }
        }
    }

fun main() {
    println(createTable())
    //<table><tr><td></td><td></td></tr></table>
}