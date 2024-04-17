package uz.hamroh.hamroh.services.impl

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.spring6.SpringTemplateEngine
import uz.hamroh.hamroh.repository.UserRepository
import uz.hamroh.hamroh.services.EmailVerificationService
import uz.hamroh.hamroh.util.OtpCodeGenerator


@Service
class EmailVerificationServiceImpl(
    private val templateEngine: SpringTemplateEngine,
    private val emailSender: JavaMailSender,
    private val userRepository: UserRepository,
): EmailVerificationService {

    override fun sendOtpCodeToEmail (email: String): String {
            try {
                val context = org.thymeleaf.context.Context()
                val otpCode = OtpCodeGenerator.getOtp()
                context.setVariable(VERIFICATION_CODE_ENV, otpCode)
                val text = templateEngine.process(EMAIL_TEMPLATE, context)
                val message = getMimeMessage()
                val helper = MimeMessageHelper(message, true, UTF_8_ENCODING)
                helper.setPriority(1)
                helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION)
                helper.setFrom(FROM_EMAIL)
                helper.setTo(email)
                helper.setText(text, true)
                emailSender.send(message)
                return otpCode
            } catch (exception: Exception) {
                println(exception.message)
                throw RuntimeException(exception.message)
            }
    }

    override fun verifyEmail(email: String) {
        val user = userRepository.findByEmail(email)
        val updatedUser = user?.copy(isVerified = true)
        updatedUser?.let { userRepository.save(it) }
    }

    private fun getMimeMessage(): MimeMessage {
        return emailSender.createMimeMessage()
    }


    private companion object {
        const val VERIFICATION_CODE_ENV = "verification_code"
        const val UTF_8_ENCODING: String = "UTF-8"
        const val EMAIL_TEMPLATE: String = "emailtemplate"
        const val FROM_EMAIL = "hamroh.go@gmail.com"
        const val NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification"
    }
}