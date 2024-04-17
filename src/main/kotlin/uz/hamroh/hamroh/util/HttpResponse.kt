package uz.hamroh.hamroh.util

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.net.URI
import java.time.LocalDateTime


@JsonInclude(JsonInclude.Include.ALWAYS)
data class HttpResponse(
    val timeStamp: String,
    val statusCode: Int,
    val status: HttpStatus,
    val success: Boolean,
    val message: String,
    val data: Map<*, *>?
)

fun createHttpResponse(
    status: HttpStatus,
    data: Map<String, Any>? = null,
    message: String
): ResponseEntity<HttpResponse> {
    return ResponseEntity.created(URI.create("")).body(
        HttpResponse(
            timeStamp = LocalDateTime.now().toString(),
            success = status == HttpStatus.OK,
            status = status,
            message = message,
            statusCode = status.value(),
            data = data
        )
    )
}