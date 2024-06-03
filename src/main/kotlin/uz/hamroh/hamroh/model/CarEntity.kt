package uz.hamroh.hamroh.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "cars")
data class CarEntity(
    @Id val id: UUID = UUID.randomUUID(),
    val model: String = "",
    val number: String = "",
    val seats: Int = 0,
    val carType: String = "",
    val color: String = "",
)
