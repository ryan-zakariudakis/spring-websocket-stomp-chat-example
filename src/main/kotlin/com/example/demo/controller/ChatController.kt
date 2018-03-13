package com.example.demo.controller

import com.example.demo.model.ChatMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller


@Controller
class ChatController {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var applicationContext: ApplicationContext
    var connectedUsers = mutableSetOf<String>()

    @MessageMapping("/userchat/{username}")
    @SendTo("/topic/userchat.{username}")
    @Throws(Exception::class)
    fun handleUserMessage(@DestinationVariable("username") username: String, message: ChatMessage): ChatMessage {
        log.info("Topic User {} says {}", username, message.message)
        return message
    }

    @MessageMapping("/userconnected/{username}")
    @SendTo("/topic/connectedusers")
    @Throws(Exception::class)
    fun userConnect(@DestinationVariable("username") username: String): String {
        log.info("User {} connected", username)
        connectedUsers.add(username)
        return connectedUsers.toString()
    }

    @MessageMapping("/whoisconnected/")
    @SendTo("/topic/connectedusers")
    @Throws(Exception::class)
    fun getConnectedUsers(): String {
        log.info("Getting connected users")
        return connectedUsers.toString()
    }

    @MessageMapping("/userdisconnected/{username}")
    @SendTo("/topic/userdisconnected")
    @Throws(Exception::class)
    fun userDisconnect(@DestinationVariable("username") username: String): String {
        log.info("User {} disconnected", username)
        connectedUsers.remove(username)
        return username
    }

}