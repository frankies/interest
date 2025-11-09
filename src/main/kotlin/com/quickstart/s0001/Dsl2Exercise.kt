open class Tag(val name: String) {
    val children = mutableListOf<Tag>()
    val attributes = mutableListOf<Attribute>()
    override fun toString(): String {
        return "<$name" +
                (if (attributes.isEmpty()) "" else attributes.joinToString(separator = "", prefix = " ")) + ">" +
                (if (children.isEmpty()) "" else children.joinToString(separator = "")) +
                "</$name>"
    }
}

class Attribute(val name: String, val value: String) {
    override fun toString() = """$name="$value" """
}

fun <T : Tag> T.set(name: String, value: String?): T {
    if (value != null) {
        attributes.add(Attribute(name, value))
    }
    return this
}

fun <T : Tag> Tag.doInit(tag: T, init: T.() -> Unit): T {
    tag.init()
    children.add(tag)
    return tag
}

class Html : Tag("html")
class Table : Tag("table")
class Center : Tag("center")
class TR : Tag("tr")
class TD : Tag("td")
class Text(val text: String) : Tag("b") {
    override fun toString() = text
}

fun html(init: Html.() -> Unit): Html =     Html().apply(init)

fun Html.table(init: Table.() -> Unit) = doInit(Table(), init)
fun Html.center(init: Center.() -> Unit) = doInit(Center(), init)

fun Table.tr(color: String? = null, init: TR.() -> Unit) = doInit(TR(), init).set("bgcolor", color)

fun TR.td(color: String? = null, align: String = "left", init: TD.() -> Unit) = doInit(TD(), init).set("align", align).set("bgcolor", color)

fun Tag.text(s: Any?) = doInit(Text(s.toString()), {})

// Product
data class Product(val description: String, val price: Double, val popularity: Int)

val cactus = Product("cactus", 11.2, 13)
val cake = Product("cake", 3.2, 111)
val camera = Product("camera", 134.5, 2)
val car = Product("car", 30000.0, 0)
val carrot = Product("carrot", 1.34, 5)
val cellPhone = Product("cell phone", 129.9, 99)
val chimney = Product("chimney", 190.0, 2)
val certificate = Product("certificate", 99.9, 1)
val cigar = Product("cigar", 8.0, 51)
val coffee = Product("coffee", 8.0, 67)
val coffeeMaker = Product("coffee maker", 201.2, 1)
val cola = Product("cola", 4.0, 67)
val cranberry = Product("cranberry", 4.1, 39)
val crocs = Product("crocs", 18.7, 10)
val crocodile = Product("crocodile", 20000.2, 1)
val cushion = Product("cushion", 131.0, 0)

fun getProducts() = listOf(cactus, cake, camera, car, carrot, cellPhone, chimney, certificate, cigar, coffee, coffeeMaker,
    cola, cranberry, crocs, crocodile, cushion)

// Data
fun getTitleColor() = "#b9c9fe"
fun getCellColor(index: Int, column: Int) = if ((index + column) % 2 == 0) "#dce4ff" else "#eff2ff"

fun renderProductTable(): String {
    return html {
        table {
            tr(color = getTitleColor()) {
                td {
                    text("Product")
                }
                td {
                    text("Price")
                }
                td {
                    text("Popularity")
                }
            }
            val products = getProducts()
            for(row in products.indices) {
                tr  {
                    td(color = getCellColor(row, 0)) { text(products[row].description) }
                    td(color = getCellColor(row, 1)) { text(products[row].price) }
                    td(color = getCellColor(row, 2)) { text(products[row].popularity) }
                }
            }
        }
    }.toString()
}

fun main() {
    println(renderProductTable())
}
