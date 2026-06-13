package site.billilge.api.backend.common.exception

class ApiException(
    val errorCode: ErrorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR,
    override val cause: Throwable? = null
): RuntimeException()
