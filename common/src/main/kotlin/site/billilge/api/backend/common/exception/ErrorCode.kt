package site.billilge.api.backend.common.exception

interface ErrorCode {
    val name: String
    val message: String
    val status: Int
}
