package com.quickstart.s0001


sealed class Payment {
    data class CreditCard(val number: String, val expiryDate: String) : Payment()
    data class PayPal(val email: String) : Payment()
    data object Cash : Payment()
}

fun processPayment(payment: Payment) {
    when (payment) {
        is Payment.CreditCard -> processCreditCardPayment(payment.number, payment.expiryDate)
        is Payment.PayPal -> processPayPalPayment(payment.email)
        is Payment.Cash -> processCashPayment()
    }
}

fun processCashPayment() {
    println("Payment of cash")
}

fun processPayPalPayment(email: String) {
    println("Payment of PayPal with email: $email")
}

fun processCreditCardPayment(number: String, expiryDate: String) {
  println("Payment of credit card: $number, expiryDate: $expiryDate")
}

///
fun main() {
    processPayment(Payment.CreditCard("12233-4443-44", "2014-03-02" ))
    processPayment(Payment.PayPal("12233@163.com"))
    processPayment(Payment.Cash)
}
