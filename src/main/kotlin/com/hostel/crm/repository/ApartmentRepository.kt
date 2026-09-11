package com.hostel.crm.repository

import com.hostel.crm.entity.Apartment
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface ApartmentRepository : JpaRepository<Apartment, Long> {
    @EntityGraph(attributePaths = ["category"])
    override fun findAll(): List<Apartment>
    fun existsByApartmentNumber(apartmentNumber: String): Boolean
}