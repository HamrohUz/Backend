package uz.hamroh.hamroh.services

import uz.hamroh.hamroh.dto.TripDto
import uz.hamroh.hamroh.model.TripEntity
import java.util.*

interface TripService {
    fun createTrip(tripRequestDTO: TripDto.TripRequest): TripDto.TripResponse
    fun getTripsByUserId(userId: UUID): List<TripDto.TripResponse>
    fun getOutdatedTrips(): List<TripDto.TripResponse>
}