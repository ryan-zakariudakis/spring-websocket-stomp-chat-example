package com.example.demo.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
const val DEFAULT_STOMP_BROKER_PORT = 61613
const val DEFAULT_STOMP_BROKER_HOST = "localhost"
const val DEFAULT_STOMP_BROKER_USERNAME = "guest"
const val DEFAULT_STOMP_BROKER_PASSWORD = "guest"
@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    private val log = LoggerFactory.getLogger(WebSocketConfig::class.java)

    @Value("\${destination.stomp.prefixes}")
    lateinit var destinationPrefixes: Array<String>
    @Value("\${destination.stomp.simplebroker:true}")
    var useStompSimpleBroker: Boolean = true
    @Value("\${destination.rabbitmq.host:$DEFAULT_STOMP_BROKER_HOST}")
    lateinit var rabbitMqHost: String
    @Value("\${destination.rabbitmq.stomp.port:$DEFAULT_STOMP_BROKER_PORT}")
    var rabbitMqStompPort: Int = DEFAULT_STOMP_BROKER_PORT
    @Value("\${destination.rabbitmq.username:$DEFAULT_STOMP_BROKER_USERNAME}")
    lateinit var rabbitMqUsername: String
    @Value("\${destination.rabbitmq.password:$DEFAULT_STOMP_BROKER_PASSWORD}")
    lateinit var rabbitMqPassword: String

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config!!.setApplicationDestinationPrefixes("/app")
        log.info("Using SimpleBroker={}", if(useStompSimpleBroker) "yes" else "no")
        if (useStompSimpleBroker){
            config.enableSimpleBroker(*destinationPrefixes)
        } else {
            config.enableStompBrokerRelay(*destinationPrefixes)
                    .setRelayHost(rabbitMqHost)
                    .setRelayPort(rabbitMqStompPort)
                    .setClientLogin(rabbitMqUsername)
                    .setClientPasscode(rabbitMqPassword)
        }
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry!!.addEndpoint("/gs-guide-websocket").withSockJS()
    }
}