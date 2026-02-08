///usr/bin/env jbang "$0" "$@" ; exit $?
//KOTLIN 2.3.0
//JAVA 25
//DEPS org.jetbrains.kotlin:kotlin-stdlib:2.0.21
//DEPS com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class Person(val name: String, val age: Int)

fun main(args: Array<String>) {
    val mapper = jacksonObjectMapper()
    val person = Person("Alice", 30)
    val json = mapper.writeValueAsString(person)
    println("JSON: $json")

    val parsed = mapper.readValue<Person>(json)
    println("Parsed: $parsed")
}
