package com.hostel.crm.controller

import com.hostel.crm.dto.GuestRequest
import com.hostel.crm.dto.GuestResponse
import com.hostel.crm.service.GuestService
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
@RequestMapping("/api/guests")
class GuestController(private val guestService: GuestService) {

    @GetMapping
    fun findAll(): List<GuestResponse> =
        guestService.findAll().map(GuestResponse::from)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: GuestRequest): GuestResponse =
        GuestResponse.from(
            guestService.create(
                request.fullName!!,
                request.passport!!,
                request.photo,
                request.birthDate!!,
                request.checkInDate,
                request.checkOutDate
            )
        )

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: GuestRequest
    ): GuestResponse =
        GuestResponse.from(
            guestService.update(
                id,
                request.fullName!!,
                request.passport!!,
                request.photo,
                request.birthDate!!,
                request.checkInDate,
                request.checkOutDate
            )
        )

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = guestService.delete(id)

    @PutMapping("/{guestId}/apartment/{apartmentId}")
    fun assignApartment(
        @PathVariable guestId: Long,
        @PathVariable apartmentId: Long
    ): GuestResponse =
        GuestResponse.from(guestService.assignApartment(guestId, apartmentId))

    @GetMapping("/apartment/{apartmentId}")
    fun findByApartment(@PathVariable apartmentId: Long): List<GuestResponse> =
        guestService.findByApartment(apartmentId).map(GuestResponse::from)
}
