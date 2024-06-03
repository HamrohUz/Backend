package uz.hamroh.hamroh.dto


object AuthDto {
    data class RegisterRequest(val email: String, val password: String)
    data class LoginRequest(val email: String, val password: String)
    data class OtpCodeRequest(val email: String)
    data class EmailVerificationRequest(val email: String)
    data class ResetPasswordRequest(val email: String, val newPassword: String)
    data class LoginResponse(val name: String, val contactNumber: String, val email: String, )
}