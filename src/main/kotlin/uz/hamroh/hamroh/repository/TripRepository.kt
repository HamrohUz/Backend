package uz.hamroh.hamroh.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import uz.hamroh.hamroh.model.TripEntity
import java.time.LocalDate
import java.util.UUID

@Repository

interface TripRepository : JpaRepository<TripEntity, UUID> {
    fun findByUserId(userId: UUID): List<TripEntity>
    @Query("SELECT t FROM TripEntity t WHERE t.date < :currentDate")
    fun findOutdatedTrips(currentDate: LocalDate): List<TripEntity>
}