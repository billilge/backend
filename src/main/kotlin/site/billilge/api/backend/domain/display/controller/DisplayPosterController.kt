package site.billilge.api.backend.domain.display.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import site.billilge.api.backend.domain.display.dto.request.DisplayPosterRequest
import site.billilge.api.backend.domain.display.dto.response.DisplayPosterFindAllResponse
import site.billilge.api.backend.domain.display.facade.DisplayPosterFacade
import site.billilge.api.backend.global.annotation.OnlyAdmin

@RestController
@RequestMapping("/display/posters")
@OnlyAdmin
class DisplayPosterController(
    private val displayPosterFacade: DisplayPosterFacade,
) : DisplayPosterApi {
    @GetMapping
    override fun getAllPosters(): ResponseEntity<DisplayPosterFindAllResponse> {
        return ResponseEntity.ok(displayPosterFacade.getAllPosters())
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun addPoster(
        @RequestPart image: MultipartFile,
        @RequestPart posterRequest: DisplayPosterRequest
    ): ResponseEntity<Void> {
        displayPosterFacade.addPoster(image, posterRequest)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PatchMapping("/{posterId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    override fun updatePoster(
        @PathVariable posterId: Long,
        @RequestPart(required = false) image: MultipartFile?,
        @RequestPart posterRequest: DisplayPosterRequest
    ): ResponseEntity<Void> {
        displayPosterFacade.updatePoster(posterId, image, posterRequest)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{posterId}")
    override fun deletePoster(
        @PathVariable posterId: Long
    ): ResponseEntity<Void> {
        displayPosterFacade.deletePoster(posterId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{posterId}/activate")
    override fun activatePoster(
        @PathVariable posterId: Long
    ): ResponseEntity<Void> {
        displayPosterFacade.activatePoster(posterId)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{posterId}/deactivate")
    override fun deactivatePoster(
        @PathVariable posterId: Long
    ): ResponseEntity<Void> {
        displayPosterFacade.deactivatePoster(posterId)
        return ResponseEntity.ok().build()
    }
}
