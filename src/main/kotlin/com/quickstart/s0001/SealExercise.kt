package com.quickstart.s0001

import java.io.File
import java.nio.file.Path
import javax.sql.DataSource

// Create a sealed interface
sealed interface Error

// Create a sealed class that implements sealed interface Error
sealed class IOError: Error

// Create a singleton object implementing the 'Error' sealed interface
object RuntimeError : Error


// Open class 'CustomError' extends 'Error' and can be extended anywhere it's visible
open class CustomError(): Error

// enum class extending the sealed interface Error
enum class ErrorType : Error {
    FILE_ERROR, DATABASE_ERROR
}



////
sealed class UIState {
    data object Loading : UIState()
    data class Success(val data: String) : UIState()
    data class Error(val exception: Exception) : UIState()
}

fun updateUI(state: UIState) {
    when (state) {
        is UIState.Loading -> showLoadingIndicator()
        is UIState.Success -> showData(state.data)
        is UIState.Error -> showError(state.exception)
    }
}

fun showData(data: String) {
    TODO("Not yet implemented")
}

fun showLoadingIndicator() {
    TODO("Not yet implemented")
}

fun showError(error: Exception) {
    TODO("Not yet implemented")
}

///
enum class ErrorSeverity { MINOR, MAJOR, CRITICAL }

sealed class Error2(val severity: ErrorSeverity) {
    class FileReadError(val file: File): Error2(ErrorSeverity.MAJOR) {
        override fun toString(): String =  "Failed to read file: ${file.absolutePath}"

    }
    class DatabaseError(val source: DataSource): Error2(ErrorSeverity.CRITICAL) {
        override fun toString(): String = "Database error: ${source.toString()}"
    }
    object RuntimeError : Error2(ErrorSeverity.CRITICAL) {
        override fun toString(): String = "Runtime error"
    }
    // Additional error types can be added here
}


// Function to log errors
fun log(e: Error2) = when(e) {
    is Error2.FileReadError -> println("Error while reading file ${e.file}")
    is Error2.DatabaseError -> println("Error while reading from database ${e.source}")
    Error2.RuntimeError -> println("Runtime error")
    // No `else` clause is required because all the cases are covered
}

fun main() {
    val runtimeError = Error2.FileReadError(Path.of("test.txt").toFile())
   log(runtimeError)
}