package uz.hamroh.hamroh.controller


import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import uz.hamroh.hamroh.services.FileStorageService
import uz.hamroh.hamroh.util.HttpResponse
import uz.hamroh.hamroh.util.createHttpResponse
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@RequestMapping("/api/profile")
class ProfileController(private val fileStorageService: FileStorageService) {

    @PostMapping("/uploadAvatar")
    fun uploadAvatar(@RequestParam("file") file: MultipartFile): ResponseEntity<HttpResponse> {
        val fileName = fileStorageService.saveFile(file, "avatars")
        val fileDownloadUri = fileStorageService.getFileDownloadUri("avatars", fileName)
        val data = mapOf("fileName" to fileName, "fileDownloadUri" to fileDownloadUri)
        return createHttpResponse(HttpStatus.OK, data, "Avatar uploaded successfully")
    }

    @GetMapping("/getAvatar/{fileName:.+}")
    fun getAvatar(@PathVariable fileName: String): ResponseEntity<Resource> {
        val resource = fileStorageService.loadFileAsResource("avatars", fileName)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(Files.probeContentType(Paths.get("uploads/avatars/$fileName"))))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"")
            .body(resource)
    }

    @PostMapping("/uploadCarPhoto")
    fun uploadCarPhoto(@RequestParam("file") file: MultipartFile): ResponseEntity<HttpResponse> {
        val fileName = fileStorageService.saveFile(file, "carPhotos")
        val fileDownloadUri = fileStorageService.getFileDownloadUri("carPhotos", fileName)
        val data = mapOf("fileName" to fileName, "fileDownloadUri" to fileDownloadUri)
        return createHttpResponse(HttpStatus.OK, data, "Car photo uploaded successfully")
    }

    @GetMapping("/getCarPhoto/{fileName:.+}")
    fun getCarPhoto(@PathVariable fileName: String): ResponseEntity<Resource> {
        val resource = fileStorageService.loadFileAsResource("carPhotos", fileName)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(Files.probeContentType(Paths.get("uploads/carPhotos/$fileName"))))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"")
            .body(resource)
    }

    @PostMapping("/uploadDocument")
    fun uploadDocument(@RequestParam("file") file: MultipartFile): ResponseEntity<HttpResponse> {
        val fileName = fileStorageService.saveFile(file, "documents")
        val fileDownloadUri = fileStorageService.getFileDownloadUri("documents", fileName)
        val data = mapOf("fileName" to fileName, "fileDownloadUri" to fileDownloadUri)
        return createHttpResponse(HttpStatus.OK, data, "Document uploaded successfully")
    }

    @GetMapping("/getDocument/{fileName:.+}")
    fun getDocument(@PathVariable fileName: String): ResponseEntity<Resource> {
        val resource = fileStorageService.loadFileAsResource("documents", fileName)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(Files.probeContentType(Paths.get("uploads/documents/$fileName"))))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"")
            .body(resource)
    }


}