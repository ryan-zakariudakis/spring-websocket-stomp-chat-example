package com.example.demo.controller

import com.example.demo.model.ChatMessage
import com.example.demo.model.ChatUser
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@Controller
class SystemChatController {

    private val log = LoggerFactory.getLogger(SystemChatController::class.java)

    @Autowired
    lateinit var simpTemplate: SimpMessagingTemplate

    @PostMapping("/adhoc_chat")
    fun adhockChat(@RequestBody message: String, @RequestParam("topic") topic: String){
        simpTemplate.convertAndSend(topic, message)
    }

    @GetMapping("/send_test")
    fun testMessage(): String{
        val message = ChatMessage(ChatUser("ryan2"), "hello")
        simpTemplate.convertAndSend("/topic/userchat.ryan", message)
        return "sent_test"
    }
}

