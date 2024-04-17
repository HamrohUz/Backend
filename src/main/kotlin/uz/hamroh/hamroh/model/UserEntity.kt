package uz.hamroh.hamroh.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import net.minidev.json.annotate.JsonIgnore
import java.util.*

@Entity
@Table(name = "users")
data class UserEntity (
    @Id val userId: UUID = UUID.randomUUID(),
    val name: String = "",
    @JsonIgnore val password: String = "",
    val email: String = "",
    val contactNumber: String = "",
    val gender: Int = 0, // 0 - FEMALE , 1 - MALE
    val isVerified: Boolean = false,
)


