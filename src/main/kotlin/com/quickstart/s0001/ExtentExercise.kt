package com.quickstart.com.quickstart.s0001


data class Item(val name: String, val price: Double, val quantity: Int = 1)
fun List<Item>.totalPrice(): Double = sumOf { it.price * it.quantity }


fun main() {

    val cart = listOf(
        Item("Book", 12.5, 2),
        Item("Pen", 1.2, 5),
        Item("Headphones", 45.6)
    )

    //
    println("Total  ${cart.totalPrice()}")

    val names = cart.sortedByDescending {it.price}.joinToString { it.name }
    println("Items(order by price from high to low): $names")
}