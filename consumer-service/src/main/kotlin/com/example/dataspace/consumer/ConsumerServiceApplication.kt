package com.example.dataspace.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConsumerServiceApplication

fun main(args: Array<String>) {
    runApplication<ConsumerServiceApplication>(*args)
}
