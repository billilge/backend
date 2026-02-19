package site.billilge.api.backend.domain.display.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.billilge.api.backend.domain.display.dto.response.DisplayResponse
import site.billilge.api.backend.domain.display.facade.DisplayFacade

@RestController
@RequestMapping("/display")
class DisplayController(
    private val displayFacade: DisplayFacade,
) : DisplayApi {

    @GetMapping
    override fun getDisplay(): ResponseEntity<DisplayResponse> {
        return ResponseEntity.ok(displayFacade.getDisplay())
    }
}
