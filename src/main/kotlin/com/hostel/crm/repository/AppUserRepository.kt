package com.hostel.crm.repository

import com.hostel.crm.entity.AppUser
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface AppUserRepository : JpaRepository<AppUser, Long> {

    @EntityGraph(attributePaths = ["role"])
    override fun findAll(): List<AppUser>
    fun existsByUsername(username: String): Boolean

    @EntityGraph(attributePaths = ["role"])
    fun findByUsername(username: String): AppUser?
}
