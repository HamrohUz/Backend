package uz.hamroh.hamroh.util

import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
object OtpCodeGenerator {
    fun getOtp(): String {
        val randomNumber: Int = Random.nextInt(from = 1000, until = 10000)
        return randomNumber.toString()
    }
}