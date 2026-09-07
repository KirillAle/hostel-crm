package com.hostel.crm.dto

import com.hostel.crm.entity.CategoryName
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.NotNull


data class CategoryRequest (

    @field:NotNull
    val name: CategoryName?,

    @field:Size(max = 255)
    val description: String?
)