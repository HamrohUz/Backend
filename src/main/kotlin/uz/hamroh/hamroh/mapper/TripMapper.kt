package uz.hamroh.hamroh.mapper

import uz.hamroh.hamroh.dto.TripDto
import uz.hamroh.hamroh.model.TripEntity

fun TripEntity.toTripResponse() = TripDto.TripResponse(
    id = this.userId,
    destination = this.destination,
    departureTime = this.departureTime,
    arrivalTime = this.arrivalTime,
    passengers = this.passengers,
    originAddress = this.originAddress,
    destinationAddress = this.destinationAddress,
    cost = this.cost,
    date = this.date
)

fun TripDto.TripRequest.toTripEntity() = TripEntity(
    userId = this.id,
    destination = this.destination,
    departureTime = this.departureTime,
    arrivalTime = this.arrivalTime,
    passengers = this.passengers,
    originAddress = this.originAddress,
    destinationAddress = this.destinationAddress,
    cost = this.cost,
    date = this.date
)