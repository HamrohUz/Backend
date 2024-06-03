package uz.hamroh.hamroh.services

import uz.hamroh.hamroh.dto.CarDto
import uz.hamroh.hamroh.model.CarEntity
import java.util.*

interface CarService {
    fun getAllCars(): List<CarDto.CarResponseDto>
    fun getCarById(id: UUID): CarDto.CarResponseDto?
    fun createCar(carRequestDto: CarDto.CarRequestDto): CarDto.CarResponseDto
    fun updateCar(id: UUID, carRequestDto: CarDto.CarRequestDto): CarDto.CarResponseDto
    fun deleteCar(id: UUID)
}