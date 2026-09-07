package com.hostel.crm.dto

import com.hostel.crm.entity.Category
import com.hostel.crm.entity.CategoryName

class CategoryResponse (
    val id: Long,
    val name: CategoryName,
    val description: String?,
    ) {
    companion object {
        fun from (category: Category) = CategoryResponse(
            id = category.id!!,
            name = category.name,
            description = category.description
        )
    }
}


