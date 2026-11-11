package site.billilge.api.backend.global.external.fcm

/**
 * FCM 푸시 발송 결과.
 *
 * 호출부가 "재시도해야 하는 실패"와 "재시도해도 소용없는 실패"를 구분할 수 있도록
 * 실패를 세 종류로 나눈다.
 */
sealed interface PushResult {
    data object Success : PushResult

    /** 토큰이 더 이상 유효하지 않음 — 재시도 대신 토큰을 제거해야 한다 */
    data object InvalidToken : PushResult

    /** FCM 일시 장애·네트워크 오류 — 동일한 요청으로 재시도할 수 있다 */
    data class Retryable(val reason: String) : PushResult

    /** 페이로드 오류 등 재시도해도 결과가 같은 실패 */
    data class Permanent(val reason: String) : PushResult
}
