package com.hostel.crm.controller

import com.hostel.crm.dto.ApartmentRequest
import com.hostel.crm.dto.ApartmentResponse
import com.hostel.crm.service.ApartmentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/apartments")
class ApartmentController(private val apartmentService: ApartmentService) {
    @GetMapping
    fun findAll(): List<ApartmentResponse> =
        apartmentService.findAll().map(ApartmentResponse::from)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: ApartmentRequest): ApartmentResponse =
        ApartmentResponse.from(
            apartmentService
                .create(
                    request.apartmentNumber!!,
                    request.roomCount!!,
                    request.cleaningDate
                )
        )

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = apartmentService.delete(id)

    @GetMapping("/{id}/room-count")
    fun getRoomCount(@PathVariable id: Long): Int = apartmentService.getRoomCount(id)

    @PutMapping("/{apartmentId}/category/{categoryId}")
    fun setCategory(
        @PathVariable apartmentId: Long,
        @PathVariable categoryId: Long,
    ): ApartmentResponse = ApartmentResponse.from(apartmentService.setCategory(apartmentId, categoryId))

}