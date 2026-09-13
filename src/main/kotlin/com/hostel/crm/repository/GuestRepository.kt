package com.hostel.crm.repository

import com.hostel.crm.entity.Guest
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface GuestRepository : JpaRepository<Guest, Long> {

    fun existsByPassport(passport: String): Boolean

    fun existsByPassportAndIdNot(passport: String, id: Long): Boolean

    @EntityGraph(attributePaths = ["apartment"])
    override fun findAll(): List<Guest>

    @EntityGraph(attributePaths = ["apartment"])
    fun findAllByApartmentId(apartmentId: Long): List<Guest>
}
