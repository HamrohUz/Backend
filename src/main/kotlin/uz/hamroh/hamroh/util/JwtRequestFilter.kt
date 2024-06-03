package uz.hamroh.hamroh.util

import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import uz.hamroh.hamroh.services.JwtService

@Component
class JwtRequestFilter(private val jwtService: JwtService) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response)
            return
        }

        val token = header.substring(7)
        val claims: Claims = try {
            jwtService.extractAllClaims(token)
        } catch (e: Exception) {
            chain.doFilter(request, response)
            return
        }

        val username = claims.subject
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val authentication = UsernamePasswordAuthenticationToken(username, null, emptyList())
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        }

        chain.doFilter(request, response)
    }
}
