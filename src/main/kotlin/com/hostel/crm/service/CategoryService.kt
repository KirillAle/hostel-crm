package com.hostel.crm.service

import com.hostel.crm.entity.Category
import com.hostel.crm.entity.CategoryName
import com.hostel.crm.exception.EntityAlreadyExistException
import com.hostel.crm.exception.EntityNotFoundException
import com.hostel.crm.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CategoryService(private val categoryRepository: CategoryRepository) {
    fun findAll(): List<Category> = categoryRepository.findAll()

    @Transactional
    fun create(name: CategoryName, description: String?): Category {
        if (categoryRepository.existsByName(name)) {
            throw EntityAlreadyExistException("Category $name already exists.")
        }
        return categoryRepository.save(Category(name = name, description = description))
    }

    @Transactional
    fun delete(id: Long) {
        if (!categoryRepository.existsById(id)) {
            throw EntityNotFoundException("Category $id not found")
        }
        categoryRepository.deleteById(id)
    }


}