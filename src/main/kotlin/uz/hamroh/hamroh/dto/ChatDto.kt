package uz.hamroh.hamroh.dto


object ChatDto {
    data class MessageRequest(
        val sender: String,
        val content: String
    )

    data class MessageResponse(
        val sender: String,
        val content: String,
        val timestamp: String
    )
}