package uz.hamroh.hamroh.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Entity
@Table(name = "trips")
data class TripEntity(
    @Id val userId: UUID = UUID.randomUUID(),
    val destination: String = "",
    val departureTime: LocalTime? = null,
    val arrivalTime: LocalTime? = null,
    val passengers: Int = 0,
    val originAddress: String = "",
    val destinationAddress: String = "",
    val cost: Double = 0.0,
    val date: LocalDate? = null
)