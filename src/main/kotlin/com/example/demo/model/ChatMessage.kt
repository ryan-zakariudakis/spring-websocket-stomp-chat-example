package com.example.demo.model

import java.io.Serializable

data class ChatMessage(val chatUser: ChatUser, val message: String = ""): Serializable