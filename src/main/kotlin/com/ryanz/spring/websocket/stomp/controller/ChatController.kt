package com.ryanz.spring.websocket.stomp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ryanz.spring.websocket.stomp.model.ChatMessage
import com.ryanz.spring.websocket.stomp.model.ChatUser
import com.ryanz.spring.websocket.stomp.model.ConnectedChatUsers
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


@Controller
class ChatController {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var stompSessionService: StompSessionService
    @Autowired
    lateinit var stompMessageService: StompMessageService

    @Autowired
    lateinit var simpTemplate: SimpMessagingTemplate

    @MessageMapping("/userchat/{username}")
    @SendTo("/topic/messages.{username}")
    @Throws(Exception::class)
    fun handleUserMessage(@DestinationVariable("username") username: String, @Payload payload: String, headerAccessor: SimpMessageHeaderAccessor): ChatMessage {
        val message: ChatMessage
        try {
            message = ObjectMapper().readValue(payload, ChatMessage::class.java)
        } catch (e: Exception){
            log.error("Could not convert payload to ChatMessage")
            throw RuntimeException("Could not convert payload to Type ChatMessage Reason="+e.localizedMessage)
        }
        log.info("Topic User {} says {} to {} ", headerAccessor.getFirstNativeHeader("username"), message.message, username)
        stompMessageService.addNewMessage(message)
        simpTemplate.convertAndSend("/topic/messages."+message.fromChatUser.username, message)
        return message
    }

    @MessageMapping("/whoisconnected/")
    @SendTo("/topic/connectedusers")
    @Throws(Exception::class)
    fun getConnectedUsers(): ConnectedChatUsers {
        log.info("Getting connected users")
        val connectedChatUsers = stompSessionService.connectedUsers().map { userId -> ChatUser(userId) }
        return ConnectedChatUsers(connectedChatUsers)
    }

}