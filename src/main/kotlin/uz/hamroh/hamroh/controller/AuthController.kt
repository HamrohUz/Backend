package uz.hamroh.hamroh.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*
import uz.hamroh.hamroh.dto.AuthDto
import uz.hamroh.hamroh.model.UserEntity
import uz.hamroh.hamroh.services.HashService
import uz.hamroh.hamroh.services.JwtService
import uz.hamroh.hamroh.services.UserService
import uz.hamroh.hamroh.util.HttpResponse
import uz.hamroh.hamroh.util.OtpCodeGenerator
import uz.hamroh.hamroh.util.PasswordChangeStatus
import uz.hamroh.hamroh.util.createHttpResponse


@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val hashService: HashService,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) {

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: AuthDto.RegisterRequest): ResponseEntity<HttpResponse> {
        if (userService.existsByEmail(registerRequest.email).not()) {
            val encryptedPassword = hashService.hashBcrypt(registerRequest.password)
            userService.saveUser(
                UserEntity(
                    email = registerRequest.email,
                    password = encryptedPassword,
                    isVerified = false,
                )
            )
            return createHttpResponse(status = HttpStatus.OK, message = "Account is created")
        } else {
            return createHttpResponse(HttpStatus.CONFLICT, message = "This email is used already")
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: AuthDto.LoginRequest): ResponseEntity<HttpResponse> {
        val isRegisteredUser = userService.existsByEmail(loginRequest.email)

        if (!isRegisteredUser) {
            return createHttpResponse(
                status = HttpStatus.NOT_ACCEPTABLE,
                message = "You need to sign up"
            )
        }

        val foundUser = userService.findByEmail(loginRequest.email)
        val isPasswordMatched = hashService.checkBcrypt(loginRequest.password, foundUser?.password ?: "")
        return when {
           isPasswordMatched && foundUser?.isVerified == true -> {
               authenticationManager.authenticate(
                   UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
               )
               val userDetails = userDetailsService.loadUserByUsername(loginRequest.email)
               val jwt = jwtService.generateToken(userDetails.username)
                createHttpResponse(
                    status = HttpStatus.OK,
                    data = mapOf(
                        "user" to AuthDto.LoginResponse(
                            name = foundUser.name,
                            contactNumber = foundUser.contactNumber,
                            email = foundUser.email
                        ),
                        "token" to jwt
                    ),
                    message = "Successfully logged in"
                )
            }

            !isPasswordMatched -> createHttpResponse(status = HttpStatus.UNAUTHORIZED, message = "Invalid password")

            foundUser?.isVerified == false -> {
                createHttpResponse(
                    status = HttpStatus.NOT_ACCEPTABLE,
                    message = "Email is not verified"
                )
            }
            else -> {
                createHttpResponse(
                    status = HttpStatus.NOT_FOUND,
                    message = "Unexpected error"
                )
            }
        }
    }

    @PostMapping("/send-verification-code")
    fun verify(@RequestBody verifyRequest: AuthDto.OtpCodeRequest): ResponseEntity<HttpResponse> {
        val otp = OtpCodeGenerator.getOtp()
        userService.sendOtpCodeToEmail(verifyRequest.email, otp)
        return createHttpResponse(
            status = HttpStatus.OK,
            data = mapOf("otp" to otp),
            message = "Otp code is sent to email"
        )
    }

    @PostMapping("/verify-email")
    fun verify(@RequestBody verifyRequest: AuthDto.EmailVerificationRequest): ResponseEntity<HttpResponse> {
        if (userService.existsByEmail(verifyRequest.email)) {
            userService.verifyEmail(verifyRequest.email)
            val jwt = jwtService.generateToken(verifyRequest.email)
            return createHttpResponse(
                status = HttpStatus.OK,
                message = "Email is verified",
                data = mapOf("token" to jwt)
            )
        } else {
            return createHttpResponse(
                status = HttpStatus.NOT_FOUND,
                message = "Email is not found"
            )
        }
    }

    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody resetPasswordRequest: AuthDto.ResetPasswordRequest): ResponseEntity<HttpResponse> {
        if (userService.existsByEmail(resetPasswordRequest.email)) {
            val status = userService.changePassword(
                email = resetPasswordRequest.email,
                newPassword = resetPasswordRequest.newPassword
            )
            return when (status) {
                PasswordChangeStatus.INVALID_PASSWORD -> createHttpResponse(
                    HttpStatus.BAD_REQUEST,
                    message = "Old password does not match"
                )

                PasswordChangeStatus.UPDATE_SUCCESS -> createHttpResponse(
                    HttpStatus.OK,
                    message = "Password is updated"
                )
            }
        } else {
            return createHttpResponse(
                status = HttpStatus.NOT_FOUND,
                message = "Email is not found"
            )
        }
    }

    @GetMapping("/check-token")
    fun checkToken(@RequestHeader("Authorization") token: String): ResponseEntity<HttpResponse> {
        val jwtToken = token.substringAfter("Bearer ")
        return try {
            val refreshedToken = jwtService.refreshToken(jwtToken) ?: ""
            createHttpResponse(
                status = HttpStatus.OK,
                message = "Token is valid",
                data = mapOf("token" to refreshedToken)
            )
        } catch (e: Exception) {
            createHttpResponse(
                status = HttpStatus.UNAUTHORIZED,
                message = "Token is invalid or expired"
            )
        }
    }
}