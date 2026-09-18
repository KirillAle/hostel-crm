package com.hostel.crm.service

import com.hostel.crm.exception.EntityAlreadyExistException
import com.hostel.crm.exception.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@Transactional
class GuestServiceTest {

    @field:Autowired
    lateinit var guestService: GuestService

    @field:Autowired
    lateinit var apartmentService: ApartmentService

    private val birthDate: LocalDate = LocalDate.of(1990, 5, 12)

    @Test
    fun `create saves guest`() {
        val guest = guestService.create("Ivan Ivanov", "1111", null, birthDate, null, null)

        assertNotNull(guest.id)
        assertEquals("Ivan Ivanov", guest.fullName)
        assertEquals("1111", guest.passport)
    }

    @Test
    fun `create duplicate passport throws`() {
        guestService.create("Ivan Ivanov", "1111", null, birthDate, null, null)

        assertFailsWith<EntityAlreadyExistException> {
            guestService.create("Petr Petrov", "1111", null, birthDate, null, null)
        }
    }

    @Test
    fun `update changes name`() {
        val guest = guestService.create("Ivan Ivanov", "1111", null, birthDate, null, null)

        val updated = guestService.update(
            guest.id!!,
            "Ivan Sidorov",
            "1111",
            "/photo.jpg",
            birthDate,
            null,
            null
        )

        assertEquals("Ivan Sidorov", updated.fullName)
        assertEquals("/photo.jpg", updated.photo)
    }

    @Test
    fun `update missing throws`() {
        assertFailsWith<EntityNotFoundException> {
            guestService.update(999L, "Nobody", "0000", null, birthDate, null, null)
        }
    }

    @Test
    fun `update to other guest passport throws`() {
        val first = guestService.create("Ivan Ivanov", "1111", null, birthDate, null, null)
        guestService.create("Petr Petrov", "2222", null, birthDate, null, null)

        assertFailsWith<EntityAlreadyExistException> {
            guestService.update(
                first.id!!,
                "Ivan Ivanov",
                "2222",
                null,
                birthDate,
                null,
                null
            )
        }
    }

    @Test
    fun `delete missing throws`() {
        assertFailsWith<EntityNotFoundException> {
            guestService.delete(999L)
        }
    }

    @Test
    fun `assignApartment sets apartment`() {
        val guest = guestService.create("Ivan Ivanov", "1111", null, birthDate, null, null)
        val apartment = apartmentService.create("201", 2, null)

        val updated = guestService.assignApartment(guest.id!!, apartment.id!!)

        assertEquals("201", updated.apartment?.apartmentNumber)
    }

    @Test
    fun `assignApartment missing guest throws`() {
        val apartment = apartmentService.create("201", 2, null)

        assertFailsWith<EntityNotFoundException> {
            guestService.assignApartment(999L, apartment.id!!)
        }
    }

    @Test
    fun `assignApartment missing apartment throws`() {
        val guest = guestService.create("Ivan Ivanov", "1111", null, birthDate, null, null)

        assertFailsWith<EntityNotFoundException> {
            guestService.assignApartment(guest.id!!, 999L)
        }
    }

    @Test
    fun `findByApartment returns assigned guests`() {
        val guest = guestService.create("Ivan Ivanov", "1111", null, birthDate, null, null)
        val apartment = apartmentService.create("201", 2, null)
        guestService.assignApartment(guest.id!!, apartment.id!!)

        val living = guestService.findByApartment(apartment.id!!)

        assertTrue(living.any { it.passport == "1111" })
    }
}
