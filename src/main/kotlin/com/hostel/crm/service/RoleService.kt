package com.hostel.crm.service

import com.hostel.crm.entity.Role
import com.hostel.crm.entity.RoleName
import com.hostel.crm.exception.EntityAlreadyExistException
import com.hostel.crm.repository.RoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RoleService(
    private val roleRepository: RoleRepository
) {
    fun findAll(): List<Role> = roleRepository.findAll()

    @Transactional
    fun create(name: RoleName): Role {
        if (roleRepository.existsByName(name)) {
            throw EntityAlreadyExistException("Role $name already exists")
        }
        return roleRepository.save(Role(name = name))
    }
}