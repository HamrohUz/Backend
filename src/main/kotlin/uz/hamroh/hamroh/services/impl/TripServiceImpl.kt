package uz.hamroh.hamroh.services.impl

import org.springframework.stereotype.Service
import uz.hamroh.hamroh.dto.TripDto
import uz.hamroh.hamroh.model.TripEntity
import uz.hamroh.hamroh.repository.TripRepository
import uz.hamroh.hamroh.services.TripService
import java.time.LocalDate
import java.util.*

@Service
class TripServiceImpl(private val tripRepository: TripRepository): TripService {

    override fun createTrip(tripRequestDTO: TripDto.TripRequest): TripDto.TripResponse {
        val tripEntity = TripEntity(
            userId = UUID.randomUUID(),
            destination = tripRequestDTO.destination,
            departureTime = tripRequestDTO.departureTime,
            arrivalTime = tripRequestDTO.arrivalTime,
            passengers = tripRequestDTO.passengers,
            originAddress = tripRequestDTO.originAddress,
            destinationAddress = tripRequestDTO.destinationAddress,
            cost = tripRequestDTO.cost,
            date = tripRequestDTO.date
        )
        val savedTrip = tripRepository.save(tripEntity)
        return mapToDTO(savedTrip)
    }

    override fun getTripsByUserId(userId: UUID): List<TripDto.TripResponse> {
        val trips = tripRepository.findByUserId(userId)
        return trips.map { mapToDTO(it) }
    }

    override fun getOutdatedTrips(): List<TripDto.TripResponse> {
        val currentDate = LocalDate.now()
        val trips = tripRepository.findOutdatedTrips(currentDate)
        return trips.map { mapToDTO(it) }
    }

    private fun mapToDTO(tripEntity: TripEntity): TripDto.TripResponse {
        return TripDto.TripResponse(
            id = tripEntity.userId,
            destination = tripEntity.destination,
            departureTime = tripEntity.departureTime,
            arrivalTime = tripEntity.arrivalTime,
            passengers = tripEntity.passengers,
            originAddress = tripEntity.originAddress,
            destinationAddress = tripEntity.destinationAddress,
            cost = tripEntity.cost,
            date = tripEntity.date
        )
    }
}