package com.ryanz.spring.websocket.stompws.controller

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Service
@Scope("singleton")
class StompSessionService {

    private val log = LoggerFactory.getLogger(StompSessionService::class.java)


    private var connectedUsers = mutableMapOf<String, MutableSet<String>>()
    private var activeSessions = mutableMapOf<String, String>()
    fun connect(sessionId: String, userId: String){
        log.info("Connected user {} for session {}", userId, sessionId)
        activeSessions.put(sessionId, userId)
        val userSessions = connectedUsers.getOrDefault(userId, mutableSetOf(sessionId))
        connectedUsers.put(userId, userSessions)
    }
    fun getUserForSession(sessionId: String): String{
        val user= activeSessions.getValue(sessionId)
        log.info("Got User {} for sessionId {}", user, sessionId)
        return user
    }
    fun isUserConnected(userId: String): Boolean{
        val sessionIdSet = connectedUsers.getOrDefault(userId, mutableSetOf())
        if (sessionIdSet.isEmpty()){
            return true
        }
        return false
    }
    fun disconnect(sessionId: String){
        log.info("Disconnected user {} for session {}", activeSessions.getValue(sessionId), sessionId)
        val removedUserId = activeSessions.remove(sessionId) ?: return

        val sessionIdSet = connectedUsers.getOrDefault(removedUserId, mutableSetOf())
        sessionIdSet.remove(sessionId)
        connectedUsers.put(removedUserId, sessionIdSet)
    }
    fun connectedUsers(): Collection<String> {
        log.info("Fetched ConnectedUsers")
        return activeSessions.values.toSet()
    }
}