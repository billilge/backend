package site.billilge.api.backend.domain.member.enums

enum class Role(
    val key: String,
    val description: String,
) {
    USER("ROLE_USER", "사용자"),
    ADMIN("ROLE_ADMIN", "관리자")
}