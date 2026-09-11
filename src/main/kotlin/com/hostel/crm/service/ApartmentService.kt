package com.hostel.crm.service

import com.hostel.crm.entity.Apartment
import com.hostel.crm.exception.EntityAlreadyExistException
import com.hostel.crm.exception.EntityNotFoundException
import com.hostel.crm.repository.ApartmentRepository
import com.hostel.crm.repository.CategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
@Transactional(readOnly = true)
class ApartmentService(
    private val apartmentRepository: ApartmentRepository,
    private val categoryRepository: CategoryRepository
) {
    fun findAll(): List<Apartment> = apartmentRepository.findAll()

    @Transactional
    fun create(apartmentNumber: String, roomCount: Int, cleaningDate: LocalDate?): Apartment {
        if (apartmentRepository.existsByApartmentNumber(apartmentNumber)) {
            throw EntityAlreadyExistException("Apartment $apartmentNumber already exists")
        }
        return apartmentRepository.save(
            Apartment(
                apartmentNumber = apartmentNumber,
                roomCount = roomCount,
                cleaningDate = cleaningDate
            )
        )
    }

    @Transactional
    fun delete(id: Long) {
        if (!apartmentRepository.existsById(id)) {
            throw EntityNotFoundException("Apartment $id not found")
        }
        apartmentRepository.deleteById(id)
    }

    @Transactional
    fun setCategory(apartmentId: Long, categoryId: Long): Apartment {
        val apartment = apartmentRepository.findByIdOrNull(apartmentId)
            ?: throw EntityNotFoundException("Apartment $apartmentId not found")
        val category = categoryRepository.findByIdOrNull(categoryId)
            ?: throw EntityNotFoundException("Category $categoryId not found")
        apartment.category = category
        return apartmentRepository.save(apartment)
    }

    fun getRoomCount(apartmentId: Long): Int {
        val apartment = apartmentRepository.findByIdOrNull(apartmentId)
            ?: throw EntityNotFoundException("Apartment $apartmentId not found")
        return apartment.roomCount
    }

}