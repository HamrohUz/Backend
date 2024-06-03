package uz.hamroh.hamroh.services.impl

import org.springframework.stereotype.Service
import uz.hamroh.hamroh.dto.CarDto
import uz.hamroh.hamroh.model.CarEntity
import uz.hamroh.hamroh.repository.CarRepository
import uz.hamroh.hamroh.services.CarService
import java.util.*

@Service
class CarServiceImpl(private val carRepository: CarRepository) : CarService {

    override fun getAllCars(): List<CarDto.CarResponseDto> {
        return carRepository.findAll().map { mapToDto(it) }
    }

    override fun getCarById(id: UUID): CarDto.CarResponseDto? {
        val car = carRepository.findById(id).orElse(null)
        return car?.let { mapToDto(it) }
    }

    override fun createCar(carRequestDto: CarDto.CarRequestDto): CarDto.CarResponseDto {
        val carEntity = CarEntity(
            model = carRequestDto.model,
            number = carRequestDto.number,
            seats = carRequestDto.seats,
            carType = carRequestDto.carType,
            color = carRequestDto.color
        )
        val savedCar = carRepository.save(carEntity)
        return mapToDto(savedCar)
    }

    override fun updateCar(id: UUID, carRequestDto: CarDto.CarRequestDto): CarDto.CarResponseDto {
        if (carRepository.existsById(id)) {
            val carEntity = CarEntity(
                id = id,
                model = carRequestDto.model,
                number = carRequestDto.number,
                seats = carRequestDto.seats,
                carType = carRequestDto.carType,
                color = carRequestDto.color
            )
            val updatedCar = carRepository.save(carEntity)
            return mapToDto(updatedCar)
        }
        throw IllegalArgumentException("Car with id $id not found")
    }

    override fun deleteCar(id: UUID) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id)
        } else {
            throw IllegalArgumentException("Car with id $id not found")
        }
    }

    private fun mapToDto(carEntity: CarEntity): CarDto.CarResponseDto {
        return CarDto.CarResponseDto(
            id = carEntity.id,
            model = carEntity.model,
            number = carEntity.number,
            seats = carEntity.seats,
            carType = carEntity.carType,
            color = carEntity.color
        )
    }
}