package com.hostel.crm.dto

import com.hostel.crm.entity.Apartment
import java.time.LocalDate

class ApartmentResponse(

    val id: Long,
    val apartmentNumber: String,
    val roomCount: Int,
    val cleaningDate: LocalDate?,
    val category: CategoryResponse?
){
    companion object{
        fun from(apartment: Apartment) = ApartmentResponse(
            id = apartment.id!!,
            apartmentNumber = apartment.apartmentNumber,
            roomCount = apartment.roomCount,
            cleaningDate = apartment.cleaningDate,
            category = apartment.category?.let { CategoryResponse.from(it) }

        )
    }
}