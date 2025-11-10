package com.quickstart.s0001

import java.nio.file.Files
import java.nio.file.Paths


fun main() {
    val stream = Files.newInputStream(Paths.get("build.gradle"))
    stream.buffered().reader().use { reader ->
        println(reader.readText())
    }
}