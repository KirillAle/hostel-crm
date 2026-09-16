package com.hostel.crm.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserRequest(
    @field:NotBlank
    @field:Size(max = 50)
    val username: String?,

    @field:NotBlank
    @field:Size(min = 8, max = 255)
    val password: String?
)
