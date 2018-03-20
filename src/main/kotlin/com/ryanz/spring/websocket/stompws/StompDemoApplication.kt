package com.ryanz.spring.websocket.stompws

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StompDemoApplication

fun main(args: Array<String>) {
    runApplication<StompDemoApplication>(*args)
}
