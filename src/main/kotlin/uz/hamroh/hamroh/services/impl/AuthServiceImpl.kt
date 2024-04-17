package uz.hamroh.hamroh.services.impl

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import uz.hamroh.hamroh.repository.UserRepository
import uz.hamroh.hamroh.services.AuthService
import uz.hamroh.hamroh.util.PasswordChangeStatus


@Service
class AuthServiceImpl(
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val userRepository: UserRepository
): AuthService  {
    override fun changePassword(email: String, previousPassword: String, newPassword: String): PasswordChangeStatus {
        val user = userRepository.findByEmail(email)
        val isNotIdentical = bCryptPasswordEncoder.matches(previousPassword, user?.password).not()

        if (isNotIdentical) {
            return PasswordChangeStatus.INVALID_PASSWORD
        }

        val encodedPassword = bCryptPasswordEncoder.encode(newPassword)
        val updatedUser = user?.copy(password = encodedPassword)
        updatedUser?.let { userRepository.save(it) }

        return PasswordChangeStatus.UPDATE_SUCCESS
    }
}