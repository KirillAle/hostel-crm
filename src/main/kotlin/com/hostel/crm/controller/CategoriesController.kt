package com.hostel.crm.controller

import com.hostel.crm.dto.CategoryRequest
import com.hostel.crm.dto.CategoryResponse
import com.hostel.crm.service.CategoryService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController



@RestController
@RequestMapping("/api/categories")
class CategoriesController(private val categoryService: CategoryService) {

    @GetMapping
    fun findAll(): List<CategoryResponse> =
        categoryService.findAll().map(CategoryResponse::from)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CategoryRequest): CategoryResponse =
        CategoryResponse.from(categoryService.create(request.name!!, request.description))

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)

    fun delete(@PathVariable id: Long) = categoryService.delete(id)

}