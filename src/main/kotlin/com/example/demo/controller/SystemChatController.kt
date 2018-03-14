package com.example.demo.controller

import com.example.demo.model.ChatMessage
import com.example.demo.model.ChatUser
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
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
    fun adhockChat(@RequestBody message: String, @RequestParam("topic") topic: String): String {
        simpTemplate.convertAndSend(topic, message)
        return "sent_test"

    }

    @GetMapping("/send_test")
    fun testMessage(): String{
        val message = ChatMessage(chatUser = ChatUser("ryan"), message = "hello", fromChatUser = ChatUser("system"))
        simpTemplate.convertAndSend("/topic/messages.ryan", message)
        return "sent_test"
    }
}

