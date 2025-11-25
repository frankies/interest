package com.quickstart.s0001.seal

fun eval(expr: Expr): Int =
    when (expr) {
        is Num -> expr.value
        is Sum -> eval(expr.left) + eval(expr.right)
    }

sealed interface Expr
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr


fun main() {
   val r = eval(Sum(Sum(Num(1), Num(2)), Num(3)))
    println(r)
}