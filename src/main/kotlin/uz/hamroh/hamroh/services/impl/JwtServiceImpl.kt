package uz.hamroh.hamroh.services.impl

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import uz.hamroh.hamroh.services.JwtService
import java.util.*
import javax.crypto.SecretKey
import kotlin.collections.HashMap

@Service
class JwtServiceImpl : JwtService {

    companion object {
        private val SECRET_KEY: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
        private val logger = LoggerFactory.getLogger(JwtServiceImpl::class.java)
    }

    private fun getSignInKey(): SecretKey {
        return SECRET_KEY
    }

    override fun extractUsername(token: String): String {
        return extractAllClaims(token).subject
    }

    override fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    override fun extractAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    override fun generateToken(username: String): String {
        val claims = HashMap<String, Any>()
        return createToken(claims, username)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 2592000000)) // 30 days in milliseconds
            .signWith(getSignInKey())
            .compact()
    }

    override fun validateToken(token: String, username: String): Boolean {
        val extractedUsername = extractUsername(token)
        return (extractedUsername == username && !isTokenExpired(token))
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    override fun refreshToken(refreshToken: String): String? {
        val claims = extractAllClaims(refreshToken)
        if (isTokenExpired(refreshToken)) {
            throw Exception("token is expired")
        } else {
            val username = claims.subject
            return generateToken(username)
        }
    }
}