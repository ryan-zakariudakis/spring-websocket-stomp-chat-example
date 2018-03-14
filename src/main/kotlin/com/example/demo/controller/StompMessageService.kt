package com.example.demo.controller

import com.example.demo.model.ChatMessage
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
        var recipientMessageMap = unReadMessages.getOrDefault(key,mutableMapOf())
        if (recipientMessageMap == null || recipientMessageMap.isEmpty()){
            recipientMessageMap = mutableMapOf()
        }
        chatMessage.messageId = UUID.randomUUID().toString()
        recipientMessageMap.put(chatMessage.messageId, chatMessage)
        log.debug("added new message with id {}", chatMessage?.messageId)
        unReadMessages.put(key, recipientMessageMap)
    }
    fun readMessage(chatMessage: ChatMessage){
        val key = chatMessage.chatUser.username
        var recipientUnreadMessageMap = unReadMessages.getOrDefault(key, mutableMapOf())
        if (recipientUnreadMessageMap != null && recipientUnreadMessageMap.isNotEmpty()){
            val message = recipientUnreadMessageMap.remove(chatMessage.messageId)
            log.debug("marked {} as read", message?.messageId)
        }
        var recipientReadMessageMap = readMessages.getOrDefault(key, mutableMapOf())
        if (recipientReadMessageMap == null || recipientReadMessageMap.isEmpty()){
            recipientReadMessageMap = mutableMapOf()
        }
        log.debug("read {}", chatMessage)

        recipientReadMessageMap.put(chatMessage.messageId, chatMessage)
        unReadMessages.put(key, recipientReadMessageMap)
    }
}