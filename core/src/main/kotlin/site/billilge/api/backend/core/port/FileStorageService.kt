package site.billilge.api.backend.core.port

import java.util.UUID

interface FileStorageService {
    fun uploadImageFile(file: FileUploadRequest, newFileName: String = "items/${UUID.randomUUID()}"): String?
    fun deleteImageFile(fileName: String)
}

data class FileUploadRequest(
    val bytes: ByteArray,
    val originalFilename: String,
    val contentType: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FileUploadRequest) return false
        return bytes.contentEquals(other.bytes) && originalFilename == other.originalFilename && contentType == other.contentType
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + originalFilename.hashCode()
        result = 31 * result + contentType.hashCode()
        return result
    }
}
