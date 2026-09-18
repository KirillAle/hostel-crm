package com.hostel.crm.service

import com.hostel.crm.entity.RoleName
import com.hostel.crm.exception.EntityAlreadyExistException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@SpringBootTest
@Transactional
class RoleServiceTest {

    @field:Autowired
    lateinit var roleService: RoleService

    @Test
    fun `create saves role`() {
        val role = roleService.create(RoleName.ADMINISTRATOR)

        assertNotNull(role.id)
        assertEquals(RoleName.ADMINISTRATOR, role.name)
    }

    @Test
    fun `create duplicate throws`() {
        roleService.create(RoleName.ADMINISTRATOR)

        assertFailsWith<EntityAlreadyExistException> {
            roleService.create(RoleName.ADMINISTRATOR)
        }
    }
}
