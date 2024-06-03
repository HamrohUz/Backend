package uz.hamroh.hamroh.services.impl

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import uz.hamroh.hamroh.services.HashService

@Service
class HashServiceImpl(
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
): HashService {
    override fun hashBcrypt(password: String): String {
        return bCryptPasswordEncoder.encode(password)
    }

    override fun checkBcrypt(rawPassword: String, encodedPassword: String): Boolean {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword)
    }
}