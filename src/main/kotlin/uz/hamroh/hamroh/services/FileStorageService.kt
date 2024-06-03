package uz.hamroh.hamroh.services

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface FileStorageService {
    fun saveFile(file: MultipartFile, subDir: String): String
    fun getFileDownloadUri(subDir: String, fileName: String): String
    fun loadFileAsResource(subDir: String, fileName: String): Resource
}