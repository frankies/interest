///usr/bin/env jbang "$0" "$@" ; exit $?

//DEPS org.junit.jupiter:junit-jupiter-engine:5.12.2
//DEPS org.junit.platform:junit-platform-console:1.12.2

//SOURCES Calculate.java

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.platform.console.ConsoleLauncher;
import org.junit.jupiter.api.DisplayName;

// JUnit5 Test class for CalculateTest
class CalculateTest {

    Calculate calculator = new Calculate();

    @Test
    @DisplayName("Testing multiple method")
    public void testMultiple() { 
        assertEquals(20, calculator.multiple(4, 5));
        assertEquals(0, calculator.multiple(0, 5));
        assertEquals(-15, calculator.multiple(-3, 5));
    }
}
