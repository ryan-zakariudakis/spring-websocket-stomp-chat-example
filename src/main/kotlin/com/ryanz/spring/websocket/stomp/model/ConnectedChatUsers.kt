package com.ryanz.spring.websocket.stomp.model

data class ConnectedChatUsers (val chatUsers: List<ChatUser> = mutableListOf())