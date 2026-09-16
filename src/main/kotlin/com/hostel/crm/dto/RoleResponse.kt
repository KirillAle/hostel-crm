package com.hostel.crm.dto

import com.hostel.crm.entity.Role
import com.hostel.crm.entity.RoleName

data class RoleResponse(
    val id: Long,
    val name: RoleName
) {
    companion object {
        fun from(role: Role) = RoleResponse(
            id = role.id!!,
            name = role.name
        )
    }
}
