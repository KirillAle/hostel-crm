package com.hostel.crm.dto

import com.hostel.crm.entity.AppUser

data class UserResponse(
    val id: Long,
    val username: String,
    val role: RoleResponse?
) {
    companion object {
        fun from(user: AppUser) = UserResponse(
            id = user.id!!,
            username = user.username,
            role = user.role?.let(RoleResponse::from)
        )
    }
}
