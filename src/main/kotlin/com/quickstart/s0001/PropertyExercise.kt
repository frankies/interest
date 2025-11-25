package com.quickstart.com.quickstart.s0001

/// keyword field
class Scoreboard {
    var score: Int = 0
        set(value) {
            field = value // <- field
            // Adds logging when updating the value
            println("Score updated to $field")
        }
}

////
class Temperature {
    // Backing property storing temperature in Celsius
    private var _celsius: Double = 0.0

    var celsius: Double
        get() = _celsius
        set(value) { _celsius = value }

    var fahrenheit: Double
        get() = _celsius * 9 / 5 + 32
        set(value) { _celsius = (value - 32) * 5 / 9 }
}

fun main() {
    val board = Scoreboard()
    board.score = 10
    // Score updated to 10
    board.score = 20
    // Score updated to 20

    ///
    val temp = Temperature()
    temp.celsius = 25.0
    println("${temp.celsius}°C = ${temp.fahrenheit}°F")
    // 25.0°C = 77.0°F

    temp.fahrenheit = 212.0
    println("${temp.celsius}°C = ${temp.fahrenheit}°F")
    // 100.0°C = 212.0°F


    ///

}