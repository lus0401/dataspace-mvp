package com.example.dataspace.broker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BrokerServiceApplication

fun main(args: Array<String>) {
	runApplication<BrokerServiceApplication>(*args)
}
