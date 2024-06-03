package uz.hamroh.hamroh.services

import io.jsonwebtoken.Claims

interface JwtService {
    fun extractUsername(token: String): String
    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T
    fun generateToken(username: String): String
    fun validateToken(token: String, username: String): Boolean
    fun extractAllClaims(token: String): Claims
    fun refreshToken(refreshToken: String): String?
}