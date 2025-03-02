package site.billilge.api.backend.domain.notification.enums

enum class NotificationStatus(
    val title: String,
    val message: String,
    val link: String
) {
    ADMIN_RENTAL_APPLY(
        "대여 신청",
        "%s(%s) 님이 %s에 %s 대여를 신청했어요.",
        "/mobile/admin/dashboard"
    ),
    ADMIN_RENTAL_CANCEL(
        "신청 취소",
        "%s(%s) 님이 %s 대여 신청을 취소했어요.",
        "/mobile/admin/dashboard"
    ),
    ADMIN_RETURN_APPLY(
        "반납 신청",
        "%s(%s) 님이 %s 반납을 신청했어요.",
        "/mobile/admin/dashboard"
    ),
    ADMIN_RETURN_CANCEL(
        "반납 취소",
        "%s(%s) 님이 %s 반납 신청을 취소했어요.",
        "/mobile/admin/dashboard"
    ),
    USER_RENTAL_APPLY(
        "대여 신청",
        "%s 대여를 신청했어요!\n관리자가 처리할 때까지 잠시만 기다려 주세요.",
        "/mobile/history"
    ),
    USER_RENTAL_APPROVED(
        "대여 승인",
        "관리자가 %s 대여 신청을 승인했어요!\n대여 시각에 과방으로 오시면 물품을 빌릴 수 있어요.",
        "/mobile/history"
    ),
    USER_RENTAL_REJECTED(
        "대여 반려",
        "%s 대여 신청이 반려되었어요.\n자세한 사항은 소프트웨어융합대학 카카오톡에 문의해 주세요.",
        "/mobile/history"
    ),
    USER_RETURN_APPLY(
        "반납 신청",
        "%s 반납을 신청했어요!\n관리자가 처리할 때까지 잠시만 기다려 주세요.",
        "/mobile/history"
    ),
    USER_RETURN_APPROVED(
        "반납 승인",
        "관리자가 %s 반납 신청을 승인했어요!\n과방으로 오시면 물품을 반납할 수 있어요.",
        "/mobile/history"
    ),
    USER_RETURN_COMPLETED(
        "반납 완료",
        "%s 반납을 완료했어요.",
        "/mobile/history"
    );

    fun formattedMessage(vararg formatValues: String): String = message.format(*formatValues)
}