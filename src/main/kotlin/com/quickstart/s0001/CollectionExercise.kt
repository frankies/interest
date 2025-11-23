package com.quickstart.com.quickstart.s0001

data class Shop(val name: String, val customers: List<Customer>)

data class Customer(val name: String, val city: City, val orders: List<Order>) {
    override fun toString() = "$name from ${city.name} order ${orders.size}"
}

data class Order(val products: List<Product>, val isDelivered: Boolean)

data class Product(val name: String, val price: Double) {
    override fun toString() = "'$name' for $price bought "
}

data class City(val name: String) {
    override fun toString() = name
}

fun Shop.getSetOfCustomers(): Set<Customer> =  customers.toSet()

fun Shop.getCustomersSortedByOrders(): List<Customer> =  customers.sortedByDescending { it.orders.size }


fun Shop.getCustomerCities(): Set<City> = customers.map { it.city }.toSet()

// Find the customers living in a given city
fun Shop.getCustomersFrom(city: City): List<Customer> =  customers.filter { it.city == city }

// Return true if all customers are from a given city
fun Shop.checkAllCustomersAreFrom(city: City): Boolean = customers.all { it.city == city }

// Return true if there is at least one customer from a given city
fun Shop.hasCustomerFrom(city: City): Boolean = customers.any { it.city == city }

// Return the number of customers from a given city
fun Shop.countCustomersFrom(city: City): Int = customers.count { it.city == city }

// Return a customer who lives in a given city, or null if there is none
fun Shop.findCustomerFrom(city: City): Customer? = customers.find { it.city == city }


// Build a map from the customer name to the customer
fun Shop.nameToCustomerMap(): Map<String, Customer> = customers.associateBy { it.name }

// Build a map from the customer to their city
fun Shop.customerToCityMap(): Map<Customer, City> = customers.associateWith { it.city }

// Build a map from the customer name to their city
fun Shop.customerNameToCityMap(): Map<String, City> = customers.associate { it.name  to it.city }


////
fun Shop.groupCustomersByCity(): Map<City, List<Customer>> = customers.groupBy { it.city }


///
// Return customers who have more undelivered orders than delivered
fun Shop.getCustomersWithMoreUndeliveredOrders(): Set<Customer> = customers.partition {c -> c.orders.any { !it.isDelivered }}.first.toSet()

/// flatmap
// Return all products the given customer has ordered
fun Customer.getOrderedProducts1(): List<Product> = orders.flatMap(Order::products)

// Return all products that were ordered by at least one customer
fun Shop.getOrderedProducts(): Set<Product> =  customers.flatMap(Customer::getOrderedProducts1).toSet()


////
// Return a customer who has placed the maximum amount of orders
fun Shop.getCustomerWithMaxOrders(): Customer? =  customers.maxByOrNull { it.orders.size }

// Return the most expensive product that has been ordered by the given customer
fun getMostExpensiveProductBy(customer: Customer): Product? = customer.orders.flatMap(Order::products).maxByOrNull(
    Product::price)


/// Sum
// Return the sum of prices for all the products ordered by a given customer
fun moneySpentBy(customer: Customer): Double = customer.orders.flatMap(Order::products).sumOf(Product::price)


/// Fold or reduce
// Return the set of products that were ordered by all customers
fun Shop.getProductsOrderedByAll(): Set<Product> = customers.map(Customer::getOrderedProducts2).reduce { orderedByAll, customer->
    orderedByAll.intersect(customer)
}

    // customers.flatMap(Customer::orders).flatMap(Order::products).toSet()

fun Customer.getOrderedProducts2(): Set<Product> =  orders.flatMap(Order::products).toSet()




// Count the amount of times a product was ordered.
// Note that a customer may order the same product several times.
fun Shop.getNumberOfTimesProductWasOrdered2(product: Product): Int {
    return customers.flatMap(Customer::getOrderedProducts).count {it == product }
}

// Find the most expensive product among all the delivered products
// ordered by the customer. Use `Order.isDelivered` flag.
fun findMostExpensiveProductBy2(customer: Customer): Product? = customer.orders.filter(Order::isDelivered).flatMap(Order::products).maxByOrNull(Product::price)



///
// Find the most expensive product among all the delivered products
// ordered by the customer. Use `Order.isDelivered` flag.
fun findMostExpensiveProductBy(customer: Customer): Product? {
   return customer.orders.asSequence().filter(Order::isDelivered).flatMap { it.products }
        .maxByOrNull { it.price }
}

// Count the amount of times a product was ordered.
// Note that a customer may order the same product several times.
fun Shop.getNumberOfTimesProductWasOrdered(product: Product): Int {
//  return  this.customers.asSequence().flatMap(Customer::orders).count { it.products.any({i -> i == product}) }
    return customers.asSequence().flatMap(Customer::getOrderedProducts).count { it == product }
}

fun Customer.getOrderedProducts(): Sequence<Product> = orders.asSequence().flatMap(Order::products)

////

fun doSomethingWithCollection(collection: Collection<String>): Collection<String>? {

    val groupsByLength = collection.groupBy { s -> s.length }

    val maximumSizeOfGroup = groupsByLength.values.map { group -> group.size }.maxOrNull()

    return groupsByLength.values.firstOrNull { group -> group.size == maximumSizeOfGroup }
}

//
fun containsEven(collection: Collection<Int>): Boolean =
    collection.any { it % 2 == 0 }


///


fun main() {
    //
    val city = City("Xiamen")
    val pds = listOf(Product("pillow", 123.3), Product("shoe", 234.3))
    val ord1 = Order(pds, false)
    val ord2 = Order(listOf(Product("PC", 32.0)), false)
    val c = Customer("Frankies", city, listOf(ord1))
    val c2 = Customer("Haha", city, listOf(ord1, ord2))

    val shop  = Shop("shop01", listOf(c, c2))
    val customers = shop.getSetOfCustomers()
//    println(customers)

//    println(shop.getCustomersSortedByOrders())
    //

    val vs = shop.customerNameToCityMap()
    println(vs)
}





