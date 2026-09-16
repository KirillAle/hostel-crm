package com.hostel.crm.repository

import com.hostel.crm.entity.Role
import com.hostel.crm.entity.RoleName
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long> {
    fun existsByName(name: RoleName): Boolean
}
