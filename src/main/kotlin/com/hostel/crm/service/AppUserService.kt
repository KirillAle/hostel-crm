package com.hostel.crm.service

import com.hostel.crm.entity.AppUser
import com.hostel.crm.exception.EntityAlreadyExistException
import com.hostel.crm.exception.EntityNotFoundException
import com.hostel.crm.repository.AppUserRepository
import com.hostel.crm.repository.RoleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AppUserService(
    private val appUserRepository: AppUserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {


    fun findAll(): List<AppUser> = appUserRepository.findAll()

    @Transactional
    fun create(username: String, rawPassword: String): AppUser {
        if (appUserRepository.existsByUsername(username)) {
            throw EntityAlreadyExistException("User $username already exists")
        }
        return appUserRepository.save(
            AppUser(
                username = username,
                passwordHash = passwordEncoder.encode(rawPassword)
            )
        )
    }

    @Transactional
    fun delete(id: Long) {
        if (!appUserRepository.existsById(id)) {
            throw EntityNotFoundException("User $id not found")
        }
        appUserRepository.deleteById(id)
    }

    @Transactional
    fun assignRole(userId: Long, roleId: Long): AppUser {
        val user = appUserRepository.findByIdOrNull(userId)
            ?: throw EntityNotFoundException("User $userId not found")
        val role = roleRepository.findByIdOrNull(roleId)
            ?: throw EntityNotFoundException("Role $roleId not found")
        user.role = role
        return appUserRepository.save(user)
    }
}