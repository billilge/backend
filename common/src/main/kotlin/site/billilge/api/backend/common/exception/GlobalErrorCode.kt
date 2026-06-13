package site.billilge.api.backend.common.exception

enum class GlobalErrorCode(
    override val message: String,
    override val status: Int,
) : ErrorCode {
    INTERNAL_SERVER_ERROR("내부 서버 오류입니다.", 500),
    INVALID_AUTHORIZATION_HEADER("유효하지 않은 Authorization 헤더입니다.", 400),
    FORBIDDEN("접근 권한이 없습니다.", 403),
    IMAGE_UPLOAD_FAILED("이미지 업로드에 실패했습니다.", 500),
    IMAGE_NOT_FOUND("이미지 파일을 찾을 수 없습니다.", 404),
    IMAGE_DELETE_FAILED("이미지 삭제에 실패했습니다.", 500),
    INVALID_OAUTH2_PROVIDER("유효하지 않은 OAuth2 로그인 인증기관입니다.", 400),
    EXPIRED_ACCESS_TOKEN("만료된 액세스 토큰입니다.", 401),
}
