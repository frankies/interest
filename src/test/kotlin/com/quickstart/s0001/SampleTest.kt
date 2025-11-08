package com.quickstart.s0001

import com.quickstart.com.quickstart.s0001.Sample
import org.junit.jupiter.api.Assertions.*
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
        todo { val i = 1 }
    }
}