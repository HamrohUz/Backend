package uz.hamroh.hamroh.repository

import org.springframework.data.jpa.repository.JpaRepository
import uz.hamroh.hamroh.model.CarEntity
import java.util.*

interface CarRepository : JpaRepository<CarEntity, UUID>