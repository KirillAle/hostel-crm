package com.hostel.crm.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class ApartmentRequest (

    @field:NotBlank
    @field:Size(max = 20)
    val apartmentNumber: String?,

    @field:NotNull
    @field:Positive
    val roomCount: Int?,

    val cleaningDate: LocalDate?

)
