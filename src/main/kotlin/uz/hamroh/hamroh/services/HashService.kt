package uz.hamroh.hamroh.services

interface HashService {
    fun hashBcrypt(password: String): String
    fun checkBcrypt(rawPassword: String, encodedPassword: String): Boolean
}