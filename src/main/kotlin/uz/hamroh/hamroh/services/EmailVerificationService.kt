package uz.hamroh.hamroh.services

interface EmailVerificationService {
    fun sendOtpCodeToEmail(email: String): String
    fun verifyEmail(email: String)
}