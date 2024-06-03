package uz.hamroh.hamroh.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uz.hamroh.hamroh.model.MessageEntity

@Repository
interface MessageRepository : JpaRepository<MessageEntity, Long>