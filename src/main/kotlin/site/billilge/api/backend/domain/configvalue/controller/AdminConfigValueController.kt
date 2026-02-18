package site.billilge.api.backend.domain.configvalue.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import site.billilge.api.backend.domain.configvalue.dto.request.ChangeAdminPasswordRequest
import site.billilge.api.backend.domain.configvalue.dto.request.ConfigValueBulkUpdateRequest
import site.billilge.api.backend.domain.configvalue.dto.request.ConfigValueUpdateRequest
import site.billilge.api.backend.domain.configvalue.dto.response.ConfigValueDetail
import site.billilge.api.backend.domain.configvalue.dto.response.ConfigValueFindAllResponse
import site.billilge.api.backend.domain.configvalue.facade.ConfigValueFacade
import site.billilge.api.backend.domain.member.enums.Role
import site.billilge.api.backend.global.annotation.OnlyAdmin

@RestController
@RequestMapping("/admin/config-values")
@OnlyAdmin(roles = [Role.ADMIN, Role.GA])
class AdminConfigValueController(
    private val configValueFacade: ConfigValueFacade,
) : AdminConfigValueApi {

    @GetMapping
    override fun getByKey(@RequestParam key: String): ResponseEntity<ConfigValueDetail> {
        return ResponseEntity.ok(configValueFacade.getByKey(key))
    }

    @GetMapping("/bulk")
    override fun getAllByKeys(@RequestParam keys: List<String>): ResponseEntity<ConfigValueFindAllResponse> {
        return ResponseEntity.ok(configValueFacade.getAllByKeys(keys))
    }

    @PutMapping
    override fun update(@RequestBody request: ConfigValueUpdateRequest): ResponseEntity<Void> {
        configValueFacade.update(request)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/bulk")
    override fun updateAll(@RequestBody request: ConfigValueBulkUpdateRequest): ResponseEntity<Void> {
        configValueFacade.updateAll(request)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/password")
    override fun changeAdminPassword(@RequestBody request: ChangeAdminPasswordRequest): ResponseEntity<Void> {
        configValueFacade.changeAdminPassword(request)
        return ResponseEntity.ok().build()
    }
}
