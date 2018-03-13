package com.example.demo.controller

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Service
@Scope("singleton")
class StompSessionService {

    private val log = LoggerFactory.getLogger(StompSessionService::class.java)


    private var connectedUsers = mutableMapOf<String, String>()
    fun connect(sessionId: String, userId: String){
        log.info("Connected user {} for session {}", userId, sessionId)
        connectedUsers.put(sessionId, userId)
    }
    fun getUserForSession(sessionId: String): String{
        val user= connectedUsers.getValue(sessionId)
        log.info("Got User {} for sessionId {}", user, sessionId)
        return user
    }
    fun disconnect(sessionId: String){
        log.info("Disconnected user {} for session {}", connectedUsers.getValue(sessionId), sessionId)
        connectedUsers.remove(sessionId)
    }
    fun connectedUsers(): Collection<String> {
        log.info("Fetched ConnectedUsers")
        return connectedUsers.values
    }
}