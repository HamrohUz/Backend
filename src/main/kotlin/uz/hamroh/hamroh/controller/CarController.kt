package uz.hamroh.hamroh.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uz.hamroh.hamroh.dto.CarDto
import uz.hamroh.hamroh.services.CarService
import uz.hamroh.hamroh.util.HttpResponse
import uz.hamroh.hamroh.util.createHttpResponse
import java.util.*

@RestController
@RequestMapping("/api/cars")
class CarController(private val carService: CarService) {

    @GetMapping
    fun getAllCars(): ResponseEntity<HttpResponse> {
        val cars = carService.getAllCars()
        val data = mapOf("cars" to cars)
        return createHttpResponse(HttpStatus.OK, data, "Cars retrieved successfully")
    }

    @GetMapping("/{id}")
    fun getCarById(@PathVariable id: UUID): ResponseEntity<HttpResponse> {
        val car = carService.getCarById(id)
        return if (car != null) {
            val data = mapOf("car" to car)
            createHttpResponse(HttpStatus.OK, data, "Car retrieved successfully")
        } else {
            createHttpResponse(HttpStatus.NOT_FOUND, null, "Car not found")
        }
    }

    @PostMapping
    fun createCar(@RequestBody carRequestDto: CarDto.CarRequestDto): ResponseEntity<HttpResponse> {
        val createdCar = carService.createCar(carRequestDto)
        val data = mapOf("car" to createdCar)
        return createHttpResponse(HttpStatus.CREATED, data, "Car created successfully")
    }

    @PutMapping("/{id}")
    fun updateCar(@PathVariable id: UUID, @RequestBody carRequestDto: CarDto.CarRequestDto): ResponseEntity<HttpResponse> {
        return try {
            val updatedCar = carService.updateCar(id, carRequestDto)
            val data = mapOf("car" to updatedCar)
            createHttpResponse(HttpStatus.OK, data, "Car updated successfully")
        } catch (e: IllegalArgumentException) {
            createHttpResponse(HttpStatus.NOT_FOUND, null, e.message ?: "Car not found")
        }
    }

    @DeleteMapping("/{id}")
    fun deleteCar(@PathVariable id: UUID): ResponseEntity<HttpResponse> {
        return try {
            carService.deleteCar(id)
            createHttpResponse(HttpStatus.NO_CONTENT, null, "Car deleted successfully")
        } catch (e: IllegalArgumentException) {
            createHttpResponse(HttpStatus.NOT_FOUND, null, e.message ?: "Car not found")
        }
    }
}