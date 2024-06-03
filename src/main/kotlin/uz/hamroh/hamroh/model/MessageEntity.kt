package uz.hamroh.hamroh.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime
import java.util.*

@Entity
data class MessageEntity(
    @Id val userId: UUID = UUID.randomUUID(),
    val sender: String = "",
    val content: String = "",
    val timestamp: LocalDateTime = LocalDateTime.now(),
    @ManyToOne val dialog: Dialog? = null
)