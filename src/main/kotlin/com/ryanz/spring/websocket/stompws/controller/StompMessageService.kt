package com.ryanz.spring.websocket.stompws.controller

import com.ryanz.spring.websocket.stompws.model.ChatMessage
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.util.*

@Service
@Scope("singleton")
class StompMessageService {
    private val log = LoggerFactory.getLogger(StompSessionService::class.java)
    private val unReadMessages = mutableMapOf<String, MutableMap<String, ChatMessage>>()
    private val readMessages = mutableMapOf<String, MutableMap<String, ChatMessage>>()

    fun addNewMessage(chatMessage: ChatMessage){
        val key = chatMessage.chatUser.username
        val recipientMessageMap = unReadMessages.getOrDefault(key,mutableMapOf())

        chatMessage.messageId = UUID.randomUUID().toString()
        recipientMessageMap.put(chatMessage.messageId, chatMessage)
        log.debug("added new message with id {}", chatMessage.messageId)
        unReadMessages.put(key, recipientMessageMap)
    }
    fun readMessage(chatMessage: ChatMessage){
        val key = chatMessage.chatUser.username
        val recipientUnreadMessageMap = unReadMessages.getOrDefault(key, mutableMapOf())
        if (recipientUnreadMessageMap.isNotEmpty()){
            val message = recipientUnreadMessageMap.remove(chatMessage.messageId)
            log.debug("marked {} as read", message?.messageId)
        }
        val recipientReadMessageMap = readMessages.getOrDefault(key, mutableMapOf())

        log.debug("read {}", chatMessage)

        recipientReadMessageMap.put(chatMessage.messageId, chatMessage)
        unReadMessages.put(key, recipientReadMessageMap)
    }
}