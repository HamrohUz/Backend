package uz.hamroh.hamroh.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.util.*

@Entity
data class Dialog(
    @Id val id: UUID = UUID.randomUUID(),
    val participants: String = "",
    @OneToMany(mappedBy = "dialog")
    val messages: List<MessageEntity> = mutableListOf()
)