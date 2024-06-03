package uz.hamroh.hamroh.services

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import uz.hamroh.hamroh.model.UserEntity
import uz.hamroh.hamroh.repository.UserRepository

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user: UserEntity = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("User not found with email: $username")

        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            emptyList()
        )
    }
}