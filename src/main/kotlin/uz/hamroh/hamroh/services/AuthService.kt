package uz.hamroh.hamroh.services

import uz.hamroh.hamroh.util.PasswordChangeStatus


interface AuthService {
    fun changePassword(email: String, previousPassword: String, newPassword: String): PasswordChangeStatus
}