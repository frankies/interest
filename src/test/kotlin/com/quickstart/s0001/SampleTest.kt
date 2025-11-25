package com.quickstart.s0001


import org.junit.jupiter.api.Test
import kotlin.test.expect
import kotlin.test.todo

class SampleTest {

    @Test
    fun sum() {
        val sample = Sample()
        val expected = 12
//        assertEquals(expected, sample.sum(4, 8))
        expect(expected, "Sum is ok!") { sample.sum(4, 8) }
        todo { println("TODo is OK!") }
    }
}