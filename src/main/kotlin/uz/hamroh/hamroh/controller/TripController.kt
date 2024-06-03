package uz.hamroh.hamroh.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uz.hamroh.hamroh.dto.TripDto
import uz.hamroh.hamroh.mapper.toTripEntity
import uz.hamroh.hamroh.mapper.toTripResponse
import uz.hamroh.hamroh.repository.TripRepository
import uz.hamroh.hamroh.util.HttpResponse
import uz.hamroh.hamroh.util.createHttpResponse
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/api/trip")
class TripController(private val tripRepository: TripRepository) {

    @GetMapping("/get-all")
    fun getAllTrips(): ResponseEntity<HttpResponse> {
        val trips = tripRepository.findAll().map { trip -> trip.toTripResponse() }
        return createHttpResponse(
            status = HttpStatus.OK,
            message = "",
            data = mapOf("trips" to trips)
        )
    }

    @GetMapping("/{id}")
    fun getTripById(@PathVariable id: UUID): ResponseEntity<HttpResponse> {
        val trip = tripRepository.findById(id)
        return if (trip.isPresent) {
            createHttpResponse(
                status = HttpStatus.OK,
                message = "",
                data = mapOf("trip" to trip.get().toTripResponse())
            )
        } else {
            createHttpResponse(
                status = HttpStatus.NOT_FOUND,
                message = "Trip not found"
            )
        }
    }

    @PostMapping("/create")
    fun createTrip(@RequestBody createTripRequest: TripDto.TripRequest): ResponseEntity<HttpResponse> {
        val tripHistory = createTripRequest.toTripEntity()
        val savedTrip = tripRepository.save(tripHistory)
        return createHttpResponse(
            status = HttpStatus.CREATED,
            message = "Trip created",
            data = mapOf("trip" to savedTrip.toTripResponse())
        )
    }

    @PutMapping("/update-trip")
    fun updateTrip(@RequestBody updatedTrip: TripDto.TripRequest): ResponseEntity<HttpResponse> {
        return if (tripRepository.existsById(updatedTrip.id)) {
            val tripToUpdate = updatedTrip.toTripEntity()
            val savedTrip = tripRepository.save(tripToUpdate)
            return createHttpResponse(
                status = HttpStatus.OK,
                message = "Trip updated",
                data = mapOf("trip" to savedTrip.toTripResponse())
            )
        } else {
            createHttpResponse(
                status = HttpStatus.NOT_FOUND,
                message = "Trip not found"
            )
        }
    }

    @DeleteMapping("/delete-trip/{id}")
    fun deleteTrip(@PathVariable id: UUID): ResponseEntity<HttpResponse> {
        return if (tripRepository.existsById(id)) {
            tripRepository.deleteById(id)
            createHttpResponse(
                status = HttpStatus.NO_CONTENT,
                message = "Trip is deleted"
            )
        } else {
            createHttpResponse(
                status = HttpStatus.NOT_FOUND,
                message = "Trip is not found"
            )
        }
    }

    @GetMapping("/outdated")
    fun getOutdatedTrips(): ResponseEntity<HttpResponse> {
        val trips = tripRepository.findOutdatedTrips(LocalDate.now())
        val data = mapOf("trips" to trips)
        return createHttpResponse(HttpStatus.OK, data, "Outdated trips retrieved successfully")
    }

}
