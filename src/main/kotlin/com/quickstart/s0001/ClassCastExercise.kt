package com.quickstart.com.quickstart.s0001

interface Expr
class Num(val value: Int): Expr
class Sum(val left: Expr, val right: Expr): Expr
fun eval(exp: Expr) : Int {
    if(exp is Num) {
        return exp.value
    }

    if(exp is Sum) {
        return eval(exp.left) + eval(exp.right)
    }

    throw IllegalArgumentException("Unknown expression")
}

fun main() {
    val res = eval(Sum(Sum(Num(1), Num(2)), Num(3)))
    assert(res == 6)
    println(res)
}