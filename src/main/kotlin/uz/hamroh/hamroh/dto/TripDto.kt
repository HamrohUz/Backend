package uz.hamroh.hamroh.dto

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

object TripDto {
    data class TripRequest(
        val id: UUID,
        val destination: String,
        val departureTime: LocalTime?,
        val arrivalTime: LocalTime?,
        val passengers: Int,
        val originAddress: String,
        val destinationAddress: String,
        val cost: Double,
        val date: LocalDate?
    )

    data class TripResponse(
        val id: UUID,
        val destination: String,
        val departureTime: LocalTime?,
        val arrivalTime: LocalTime?,
        val passengers: Int,
        val originAddress: String,
        val destinationAddress: String,
        val cost: Double,
        val date: LocalDate?
    )
}
