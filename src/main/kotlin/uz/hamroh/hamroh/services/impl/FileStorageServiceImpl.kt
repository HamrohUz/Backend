package uz.hamroh.hamroh.services.impl

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import uz.hamroh.hamroh.services.FileStorageService
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileStorageServiceImpl: FileStorageService {

    private val fileStorageLocation: Path = Paths.get("uploads").toAbsolutePath().normalize()

    init {
        Files.createDirectories(fileStorageLocation)
    }

    override fun saveFile(file: MultipartFile, subDir: String): String {
        val fileName = file.originalFilename ?: throw RuntimeException("Invalid file name")
        val targetLocation = fileStorageLocation.resolve(subDir).resolve(fileName)
        Files.createDirectories(targetLocation.parent)
        Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
        return fileName
    }

    override fun getFileDownloadUri(subDir: String, fileName: String): String {
        return "/api/downloadFile/$subDir/$fileName"
    }

    override fun loadFileAsResource(subDir: String, fileName: String): Resource {
        val filePath = fileStorageLocation.resolve(subDir).resolve(fileName).normalize()
        return UrlResource(filePath.toUri())
    }
}