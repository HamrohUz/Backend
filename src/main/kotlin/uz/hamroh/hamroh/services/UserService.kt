package uz.hamroh.hamroh.services

import uz.hamroh.hamroh.model.UserEntity
import uz.hamroh.hamroh.util.PasswordChangeStatus

interface UserService {
    fun saveUser(userEntity: UserEntity)
    fun findByEmail(email: String): UserEntity?
    fun existsByEmail(email: String): Boolean
    fun sendOtpCodeToEmail(email: String): String
    fun changePassword(email: String, previousPassword: String, newPassword: String): PasswordChangeStatus
    fun verifyEmail(email:String)
}