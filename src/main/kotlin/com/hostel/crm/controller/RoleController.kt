package com.hostel.crm.controller

import com.hostel.crm.dto.RoleRequest
import com.hostel.crm.dto.RoleResponse
import com.hostel.crm.service.RoleService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/roles")
class RoleController(private val roleService: RoleService) {

    @GetMapping
    fun findAll(): List<RoleResponse> =
        roleService.findAll().map(RoleResponse::from)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: RoleRequest): RoleResponse =
        RoleResponse.from(roleService.create(request.name!!))
}
