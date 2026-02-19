package site.billilge.api.backend.domain.configvalue.entity

import jakarta.persistence.*

@Entity
@Table(name = "admin_config_values")
class ConfigValue(
    @Column(name = "config_key", nullable = false, unique = true)
    val key: String,
    @Column(name = "config_value", nullable = false)
    var value: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateValue(value: String) {
        this.value = value
    }
}
