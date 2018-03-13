package com.example.demo.controller.listener

import com.example.demo.controller.StompSessionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.*

const private val usernameHeader = "username"


@Component
class StompSubscribeListener: ApplicationListener<SessionSubscribeEvent>{
    private val log = LoggerFactory.getLogger(StompSubscribeListener::class.java)

    @Autowired
    lateinit var stompSessionService: StompSessionService

    @Autowired
    lateinit var simpTemplate: SimpMessagingTemplate

    override fun onApplicationEvent(event: SessionSubscribeEvent) {
        try {
            val sha = StompHeaderAccessor.wrap(event.message)
            val user = stompSessionService.getUserForSession(sha.sessionId!!)
            log.debug("Connected event [sessionId: " + sha.sessionId + "; user: " + user + " ]")
        }catch (e: Exception) {
            log.error(e.localizedMessage, e)
        }
    }

}

@Component
class StompSessionConnectedListener: ApplicationListener<SessionConnectedEvent>{
    private val log = LoggerFactory.getLogger(StompSessionConnectedListener::class.java)

    @Autowired
    lateinit var stompSessionService: StompSessionService

    @Autowired
    lateinit var simpTemplate: SimpMessagingTemplate

    override fun onApplicationEvent(event: SessionConnectedEvent) {
        try {
            val sha = StompHeaderAccessor.wrap(event.message)
            val user = stompSessionService.getUserForSession(sha.sessionId!!)
            log.debug("Connected event [sessionId: " + sha.sessionId + "; user: " + user + " ]")
        } catch (e: Exception) {
            log.error(e.localizedMessage, e)
        }
    }

}

@Component
class StompSessionConnectListener: ApplicationListener<SessionConnectEvent>{

    private val log = LoggerFactory.getLogger(StompSessionConnectListener::class.java)

    @Autowired
    lateinit var stompSessionService: StompSessionService

    @Autowired
    lateinit var simpTemplate: SimpMessagingTemplate

    override fun onApplicationEvent(event: SessionConnectEvent) {
        try {
            val sha = StompHeaderAccessor.wrap(event.message)
            val user = sha.getNativeHeader(usernameHeader)!![0]

            stompSessionService.connect(sha.sessionId!!, user)
            simpTemplate.convertAndSend("/topic/connectedusers", stompSessionService.connectedUsers().toString())
            log.debug("Connected event [sessionId: " + sha.sessionId + "; user: " + user + " ]")
        } catch (e: Exception) {
            log.error(e.localizedMessage, e)
        }
    }

}

@Component
class StompSessionUnsubscribeListener: ApplicationListener<SessionUnsubscribeEvent>{
    private val log = LoggerFactory.getLogger(StompSessionUnsubscribeListener::class.java)

    @Autowired
    lateinit var stompSessionService: StompSessionService

    @Autowired
    lateinit var simpTemplate: SimpMessagingTemplate


    override fun onApplicationEvent(event: SessionUnsubscribeEvent) {
        try {
            val sha = StompHeaderAccessor.wrap(event.message)
            val user = stompSessionService.getUserForSession(sha.sessionId!!)
            log.debug("Connected event [sessionId: " + sha.sessionId + "; user: " + user + " ]")
        }catch (e: Exception) {
            log.error(e.localizedMessage, e)
        }
    }

}

@Component
class StompSessionDisconnectListener: ApplicationListener<SessionDisconnectEvent>{
    private val log = LoggerFactory.getLogger(StompSessionDisconnectListener::class.java)

    @Autowired
    lateinit var stompSessionService: StompSessionService

    @Autowired
    lateinit var simpTemplate: SimpMessagingTemplate

    override fun onApplicationEvent(event: SessionDisconnectEvent) {
        try {
            val sha = StompHeaderAccessor.wrap(event.message)
            val user = stompSessionService.getUserForSession(sha.sessionId!!)
            stompSessionService.disconnect(sha.sessionId!!)
            simpTemplate.convertAndSend("/topic/connectedusers", stompSessionService.connectedUsers().toString())
            log.debug("Connected event [sessionId: " + sha.getSessionId() + "; user: " + user + " ]")
        }catch (e: Exception) {
            log.error(e.localizedMessage, e)
        }
    }

}