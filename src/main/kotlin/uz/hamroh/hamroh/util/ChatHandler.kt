package uz.hamroh.hamroh.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import uz.hamroh.hamroh.dto.ChatDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class ChatHandler : TextWebSocketHandler() {

    private val sessions = mutableListOf<WebSocketSession>()
    private val objectMapper = ObjectMapper()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val chatMessage = objectMapper.readValue(message.payload, ChatDto.MessageRequest::class.java)
        val response = ChatDto.MessageResponse(
            sender = chatMessage.sender,
            content = chatMessage.content,
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
        val responseMessage = objectMapper.writeValueAsString(response)
        sessions.forEach { it.sendMessage(TextMessage(responseMessage)) }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: org.springframework.web.socket.CloseStatus) {
        sessions.remove(session)
    }
}