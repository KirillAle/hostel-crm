package com.hostel.crm.controller

import com.hostel.crm.dto.UserRequest
import com.hostel.crm.dto.UserResponse
import com.hostel.crm.service.AppUserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val appUserService: AppUserService) {

    @GetMapping
    fun findAll(): List<UserResponse> =
        appUserService.findAll().map(UserResponse::from)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: UserRequest): UserResponse =
        UserResponse.from(
            appUserService.create(request.username!!, request.password!!)
        )

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = appUserService.delete(id)

    @PutMapping("/{userId}/role/{roleId}")
    fun assignRole(
        @PathVariable userId: Long,
        @PathVariable roleId: Long
    ): UserResponse =
        UserResponse.from(appUserService.assignRole(userId, roleId))
}
