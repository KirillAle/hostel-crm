package com.hostel.crm.service

import com.hostel.crm.entity.Guest
import com.hostel.crm.exception.EntityAlreadyExistException
import com.hostel.crm.exception.EntityNotFoundException
import com.hostel.crm.repository.ApartmentRepository
import com.hostel.crm.repository.GuestRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class GuestService(
    private val guestRepository: GuestRepository,
    private val apartmentRepository: ApartmentRepository,
) {

    fun findAll(): List<Guest> = guestRepository.findAll()

    @Transactional
    fun create(
        fullName: String,
        passport: String,
        photo: String?,
        birthDate: LocalDate,
        checkInDate: LocalDate?,
        checkOutDate: LocalDate?
    ): Guest {
        if (guestRepository.existsByPassport(passport)) {
            throw EntityAlreadyExistException("Guest with passport $passport already exists")
        }
        return guestRepository.save(
            Guest(
                fullName = fullName,
                passport = passport,
                photo = photo,
                birthDate = birthDate,
                checkInDate = checkInDate,
                checkOutDate = checkOutDate
            )
        )
    }

    @Transactional
    fun update(
        id: Long,
        fullName: String,
        passport: String,
        photo: String?,
        birthDate: LocalDate,
        checkInDate: LocalDate?,
        checkOutDate: LocalDate?
    ): Guest {
        val guest = guestRepository.findByIdOrNull(id) ?: throw EntityNotFoundException("Guest $id not found")
        if (guestRepository.existsByPassportAndIdNot(passport, id)) {
            throw EntityAlreadyExistException("Guest with passport $passport already exists")
        }
        guest.fullName = fullName
        guest.passport = passport
        guest.photo = photo
        guest.birthDate = birthDate
        guest.checkInDate = checkInDate
        guest.checkOutDate = checkOutDate

        return guestRepository.save(guest)

    }

    @Transactional
    fun delete(id: Long) {
        if (!guestRepository.existsById(id)) {
            throw EntityNotFoundException("Guest $id not found")
        }
        guestRepository.deleteById(id)
    }

    @Transactional
    fun assignApartment(guestId: Long, apartmentId: Long): Guest {
        val guest = guestRepository.findByIdOrNull(guestId)
            ?: throw EntityNotFoundException("Guest $guestId not found")
        val apartment = apartmentRepository.findByIdOrNull(apartmentId)
            ?: throw EntityNotFoundException("Apartment $apartmentId not found")
        guest.apartment = apartment
        return guestRepository.save(guest)
    }

    fun findByApartment(apartmentId: Long): List<Guest> =
        guestRepository.findAllByApartmentId(apartmentId)
}