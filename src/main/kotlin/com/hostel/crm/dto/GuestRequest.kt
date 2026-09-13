package com.hostel.crm.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate


data class GuestRequest(
    @field:NotBlank
    @field:Size(max = 255)
    val fullName: String?,

    @field:NotBlank
    @field:Size(max = 50)
    val passport: String?,

    @field:Size(max = 500)
    val photo: String?,

    @field:NotNull
    val birthDate: LocalDate?,

    val checkInDate: LocalDate?,
    val checkOutDate: LocalDate?
)

