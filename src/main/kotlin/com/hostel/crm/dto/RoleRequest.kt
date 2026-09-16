package com.hostel.crm.dto

import com.hostel.crm.entity.RoleName
import jakarta.validation.constraints.NotNull

data class RoleRequest(
    @field:NotNull
    val name: RoleName?


)
