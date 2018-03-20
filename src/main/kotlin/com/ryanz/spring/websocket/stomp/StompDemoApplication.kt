package com.ryanz.spring.websocket.stomp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.ryanz.spring.websocket.stomp"])
class StompDemoApplication

fun main(args: Array<String>) {
    runApplication<StompDemoApplication>(*args)
}
