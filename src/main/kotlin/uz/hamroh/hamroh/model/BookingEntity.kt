package uz.hamroh.hamroh.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "booking")
data class BookingEntity(
    @Id val id: UUID = UUID.randomUUID(),
    @ManyToOne val ride: TripEntity? = null,
    @ManyToOne val passenger: UserEntity? = null,
    val status: String = ""
)