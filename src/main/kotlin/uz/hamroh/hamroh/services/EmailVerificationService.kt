package uz.hamroh.hamroh.services

interface EmailVerificationService {
    fun sendOtpCodeToEmail(email: String, otpCode: String)
    fun verifyEmail(email: String)
}