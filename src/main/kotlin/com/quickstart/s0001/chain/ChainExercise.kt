package com.quickstart.s0001.chain

import com.quickstart.s0001.myApply


//class ResultProcessor(val result: Any?, val errMsg: String? = null) {
//    fun onSuccess(b: (Any?) -> Unit): ResultProcessor {
//        b(result)
//        return this
//    }
//
//    fun onFailure(b: () -> Unit): ResultProcessor {
//        this.apply { b.invoke() }
//        return this
//    }
//}
//
//fun runCatch(b: () -> Any?): ResultProcessor {
//    try {
//       return ResultProcessor( b.invoke())
//    } catch (e: Exception) {
//        return ResultProcessor( e.localizedMessage)
//    }
//}

fun main() {
//    runCatch {
//        val x=  '2'
//    }.onFailure {
//        println("Erro"  )
//    }.onSuccess {
//        println("Success")
//    }     


    val result = runCatching {
        "123a".toInt() // risky code
    }
    result.onSuccess { println("Parsed number: $it") }
        .onFailure {
            println("Failed to parse: ${it.message}")
        }
}


