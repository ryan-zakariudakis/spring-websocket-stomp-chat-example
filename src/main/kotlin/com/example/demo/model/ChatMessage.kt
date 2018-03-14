package com.example.demo.model

import java.io.Serializable
import java.time.LocalDateTime

data class ChatMessage(var messageId: String = "", var sentDateTime: LocalDateTime = LocalDateTime.now(), var chatUser: ChatUser = ChatUser(), var message: String = "", var fromChatUser: ChatUser = ChatUser()): Serializable