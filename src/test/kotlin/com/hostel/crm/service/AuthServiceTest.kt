package com.hostel.crm.service

import com.hostel.crm.entity.RoleName
import com.hostel.crm.exception.UnauthorizedException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@SpringBootTest
@Transactional
class AuthServiceTest {

    @field:Autowired
    lateinit var authService: AuthService

    @field:Autowired
    lateinit var appUserService: AppUserService

    @field:Autowired
    lateinit var roleService: RoleService

    @Test
    fun `login returns jwt for valid user`() {
        val user = appUserService.create("loginuser", "password1")
        val role = roleService.create(RoleName.ADMINISTRATOR)
        appUserService.assignRole(user.id!!, role.id!!)

        val token = authService.login("loginuser", "password1")

        assertTrue(token.isNotBlank())
        assertTrue(token.count { it == '.' } == 2)
    }

    @Test
    fun `login unknown user throws`() {
        assertFailsWith<UnauthorizedException> {
            authService.login("nobody", "password1")
        }
    }

    @Test
    fun `login wrong password throws`() {
        val user = appUserService.create("wrongpass", "password1")
        val role = roleService.create(RoleName.ADMINISTRATOR)
        appUserService.assignRole(user.id!!, role.id!!)

        assertFailsWith<UnauthorizedException> {
            authService.login("wrongpass", "otherpass")
        }
    }

    @Test
    fun `login without role throws`() {
        appUserService.create("norolelogin", "password1")

        assertFailsWith<UnauthorizedException> {
            authService.login("norolelogin", "password1")
        }
    }
}
