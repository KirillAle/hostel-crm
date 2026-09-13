package com.hostel.crm.dto

import com.hostel.crm.entity.Guest
import java.time.LocalDate

data class GuestApartmentResponse(
    val id: Long,
    val apartmentNumber: String
)

data class GuestResponse(
    val id: Long,
    val fullName: String,
    val passport: String,
    val photo: String?,
    val birthDate: LocalDate,
    val checkInDate: LocalDate?,
    val checkOutDate: LocalDate?,
    val apartment: GuestApartmentResponse?
) {
    companion object {
        fun from(guest: Guest) = GuestResponse(
            id = guest.id!!,
            fullName = guest.fullName,
            passport = guest.passport,
            photo = guest.photo,
            birthDate = guest.birthDate,
            checkInDate = guest.checkInDate,
            checkOutDate = guest.checkOutDate,
            apartment = guest.apartment?.let {
                GuestApartmentResponse(it.id!!, it.apartmentNumber)
            }
        )
    }
}