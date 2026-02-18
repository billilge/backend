package site.billilge.api.backend.domain.configvalue.enums

enum class ConfigValueKeys(
    val key: String,
) {
    EXAM_PERIOD_START_DATE("exam-period.start-date"),
    EXAM_PERIOD_END_DATE("exam-period.end-date"),
    ADMIN_PASSWORD("login.admin-password"),
}
