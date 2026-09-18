package com.hostel.crm.service

import com.hostel.crm.entity.RoleName
import com.hostel.crm.exception.EntityAlreadyExistException
import com.hostel.crm.exception.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@Transactional
class AppUserServiceTest {

    @field:Autowired
    lateinit var appUserService: AppUserService

    @field:Autowired
    lateinit var roleService: RoleService

    @Test
    fun `create hashes password`() {
        val user = appUserService.create("hashuser", "password1")

        assertNotNull(user.id)
        assertEquals("hashuser", user.username)
        val hash = requireNotNull(user.passwordHash)
        assertNotEquals("password1", hash)
        assertTrue(hash.startsWith("\$2a\$") || hash.startsWith("\$2b\$"))
    }

    @Test
    fun `create duplicate throws`() {
        appUserService.create("same", "password1")

        assertFailsWith<EntityAlreadyExistException> {
            appUserService.create("same", "password1")
        }
    }

    @Test
    fun `delete missing throws`() {
        assertFailsWith<EntityNotFoundException> {
            appUserService.delete(999L)
        }
    }

    @Test
    fun `assignRole sets role`() {
        val user = appUserService.create("withrole", "password1")
        val role = roleService.create(RoleName.ADMINISTRATOR)

        val updated = appUserService.assignRole(user.id!!, role.id!!)

        assertEquals(RoleName.ADMINISTRATOR, updated.role?.name)
    }

    @Test
    fun `assignRole missing user throws`() {
        val role = roleService.create(RoleName.ACCOMMODATION_MANAGER)

        assertFailsWith<EntityNotFoundException> {
            appUserService.assignRole(999L, role.id!!)
        }
    }

    @Test
    fun `assignRole missing role throws`() {
        val user = appUserService.create("norole", "password1")

        assertFailsWith<EntityNotFoundException> {
            appUserService.assignRole(user.id!!, 999L)
        }
    }
}
