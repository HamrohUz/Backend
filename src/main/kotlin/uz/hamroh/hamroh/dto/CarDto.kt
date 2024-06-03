package uz.hamroh.hamroh.dto

import java.util.*

object CarDto {

    data class CarRequestDto(
        val model: String,
        val number: String,
        val seats: Int,
        val carType: String,
        val color: String
    )

    data class CarResponseDto(
        val id: UUID,
        val model: String,
        val number: String,
        val seats: Int,
        val carType: String,
        val color: String
    )
}