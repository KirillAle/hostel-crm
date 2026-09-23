package com.hostel.crm.service

import com.hostel.crm.entity.CategoryName
import com.hostel.crm.exception.EntityAlreadyExistException
import com.hostel.crm.exception.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @field:Autowired
    lateinit var categoryService: CategoryService
    @field:Autowired
    lateinit var apartmentService: ApartmentService

    @Test
    fun `create saves category`() {
        val category = categoryService.create(CategoryName.DELUXE, "Deluxe room")

        assertNotNull(category.id)
        assertEquals(CategoryName.DELUXE, category.name)
        assertEquals("Deluxe room", category.description)
    }

    @Test
    fun `create duplicate throws`() {
        categoryService.create(CategoryName.STANDARD, null)

        assertFailsWith<EntityAlreadyExistException> {
            categoryService.create(CategoryName.STANDARD, null)
        }
    }

    @Test
    fun `delete missing throws`() {
        assertFailsWith<EntityNotFoundException> {
            categoryService.delete(999L)
        }
    }

    @Test
    fun `findAll returns created category`() {
        categoryService.create(CategoryName.BUSINESS, "Business")

        val all = categoryService.findAll()

        assertTrue(all.any { it.name == CategoryName.BUSINESS })
    }

    @Test
    fun `delete category in use throws`() {
        val category = categoryService.create(CategoryName.DELUXE, "Deluxe")
        val apartment = apartmentService.create("201", 2, null)
        apartmentService.assignCategory(apartment.id!!, category.id!!)
        assertFailsWith<EntityAlreadyExistException> {
            categoryService.delete(category.id!!)
        }
    }
}
