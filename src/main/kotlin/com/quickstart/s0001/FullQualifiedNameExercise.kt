package com.quickstart.s0001


fun openFile() {
    println("Opening File in top class")
}

class Repo {
    fun openFile() {
        println("Opening File in class")
    }

    fun readFile() {
        openFile() // <-- Invoke method of this class
    }

    fun readFileFromTopClass() {
        com.quickstart.s0001.openFile() // <-- Invoke method of top class with full qualified names
    }
}

fun main() {
   Repo().readFile()
    Repo().readFileFromTopClass()
}