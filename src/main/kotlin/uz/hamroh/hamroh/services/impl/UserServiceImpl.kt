package uz.hamroh.hamroh.services.impl

import org.springframework.stereotype.Service
import uz.hamroh.hamroh.model.UserEntity
import uz.hamroh.hamroh.repository.UserRepository
import uz.hamroh.hamroh.services.AuthService
import uz.hamroh.hamroh.services.EmailVerificationService
import uz.hamroh.hamroh.services.UserService
import uz.hamroh.hamroh.util.PasswordChangeStatus


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val emailService: EmailVerificationService,
    private val authService: AuthService,
): UserService {
    override fun saveUser(userEntity: UserEntity) {
        userRepository.save(userEntity)
    }

    override fun findByEmail(email: String): UserEntity? {
        return userRepository.findByEmail(email)
    }

    override fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    override fun sendOtpCodeToEmail(email: String, otpCode: String) {
        return emailService.sendOtpCodeToEmail(email, otpCode)
    }

    override fun changePassword(email: String,  newPassword: String): PasswordChangeStatus {
        return authService.changePassword(email, newPassword)
    }

    override fun verifyEmail(email: String) {
        emailService.verifyEmail(email)
    }


}