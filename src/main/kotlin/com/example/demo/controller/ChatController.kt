package com.example.demo.controller

import com.example.demo.model.ChatMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


@Controller
class ChatController {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var stompSessionService: StompSessionService

    @Autowired
    lateinit var simpTemplate: SimpMessagingTemplate

    @MessageMapping("/userchat/{username}")
    @SendTo("/topic/userchat.{username}")
    @Throws(Exception::class)
    fun handleUserMessage(@DestinationVariable("username") username: String, message: ChatMessage): ChatMessage {
        log.info("Topic User {} says {}", username, message.message)
        return message
    }

    @MessageMapping("/whoisconnected/")
    @SendTo("/topic/connectedusers")
    @Throws(Exception::class)
    fun getConnectedUsers(): String {
        log.info("Getting connected users")
        return stompSessionService.connectedUsers().toString()
    }

}