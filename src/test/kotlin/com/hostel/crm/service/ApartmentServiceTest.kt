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

@SpringBootTest
@Transactional
class ApartmentServiceTest {

    @field:Autowired
    lateinit var apartmentService: ApartmentService

    @field:Autowired
    lateinit var categoryService: CategoryService

    @Test
    fun `create saves apartment`() {
        val apartment = apartmentService.create("101", 2, null)

        assertNotNull(apartment.id)
        assertEquals("101", apartment.apartmentNumber)
        assertEquals(2, apartment.roomCount)
    }

    @Test
    fun `create duplicate throws`() {
        apartmentService.create("101", 2, null)

        assertFailsWith<EntityAlreadyExistException> {
            apartmentService.create("101", 3, null)
        }
    }

    @Test
    fun `delete missing throws`() {
        assertFailsWith<EntityNotFoundException> {
            apartmentService.delete(999L)
        }
    }

    @Test
    fun `getRoomCount returns created value`() {
        val apartment = apartmentService.create("102", 4, null)

        assertEquals(4, apartmentService.getRoomCount(apartment.id!!))
    }

    @Test
    fun `getRoomCount missing throws`() {
        assertFailsWith<EntityNotFoundException> {
            apartmentService.getRoomCount(999L)
        }
    }

    @Test
    fun `assignCategory sets category`() {
        val apartment = apartmentService.create("103", 2, null)
        val category = categoryService.create(CategoryName.DELUXE, "Deluxe")

        val updated = apartmentService.assignCategory(apartment.id!!, category.id!!)

        assertEquals(CategoryName.DELUXE, updated.category?.name)
    }

    @Test
    fun `assignCategory missing apartment throws`() {
        val category = categoryService.create(CategoryName.STANDARD, null)

        assertFailsWith<EntityNotFoundException> {
            apartmentService.assignCategory(999L, category.id!!)
        }
    }

    @Test
    fun `assignCategory missing category throws`() {
        val apartment = apartmentService.create("104", 2, null)

        assertFailsWith<EntityNotFoundException> {
            apartmentService.assignCategory(apartment.id!!, 999L)
        }
    }
}
