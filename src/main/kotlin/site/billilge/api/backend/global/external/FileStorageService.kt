package site.billilge.api.backend.global.external

import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface FileStorageService {
    fun uploadImageFile(imageFile: MultipartFile, newFileName: String = "items/${UUID.randomUUID()}"): String?

    fun deleteImageFile(fileName: String)
}