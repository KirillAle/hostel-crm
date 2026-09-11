package com.hostel.crm.repository

import com.hostel.crm.entity.Category
import com.hostel.crm.entity.CategoryName

import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long> {
    fun existsByName(name: CategoryName): Boolean

}