package com.example.domain.invite

import com.example.domain.user.UserTable
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.test.*
import kotlin.uuid.Uuid

class InviteRepositoryImplTest {

    companion object {
        private lateinit var repository: InviteRepository

        @JvmStatic
        @org.junit.BeforeClass
        fun setUp() {
            Database.connect(
                url = "jdbc:h2:mem:invite-test;DB_CLOSE_DELAY=-1",
                driver = "org.h2.Driver",
            )

            transaction {
                SchemaUtils.create(
                    UserTable,
                    InviteTable,
                )
            }

            repository = InviteRepositoryImpl()
        }

        private fun createUser(): UInt {
            return transaction {
                UserTable.insert {
                    it[UserTable.name] = "TestUser"
                    it[UserTable.uuid] = Uuid.random()
                }[UserTable.id].value
            }
        }
    }

    @Test
    fun `초대코드를 생성할 수 있다`() {
        val userId = createUser()

        val invite = repository.create(userId)

        assertEquals(userId, invite.userId)
        assertEquals(5, invite.code.value.length)
        assertNotNull(invite.createdAt)
    }

    @Test
    fun `코드로 초대코드를 조회할 수 있다`() {
        val userId = createUser()
        val created = repository.create(userId)

        val found = repository.findByCode(created.code)

        assertNotNull(found)
        assertEquals(created, found)
    }

    @Test
    fun `존재하지 않는 코드로 조회하면 null을 반환한다`() {
        val result = repository.findByCode(
            InviteCode("ZZZZZ"),
        )

        assertNull(result)
    }

    @Test
    fun `userId로 초대코드를 조회할 수 있다`() {
        val userId = createUser()
        val created = repository.create(userId)

        val found = repository.findByUserId(userId)

        assertNotNull(found)
        assertEquals(created, found)
    }

    @Test
    fun `존재하지 않는 userId로 조회하면 null을 반환한다`() {
        val result = repository.findByUserId(999_999u)

        assertNull(result)
    }

    @Test
    fun `초대코드를 삭제할 수 있다`() {
        val userId = createUser()
        val created = repository.create(userId)

        repository.delete(created.code)

        val found = repository.findByCode(created.code)

        assertNull(found)
    }

    @Test
    fun `한 유저는 하나의 초대코드만 생성할 수 있다`() {
        val userId = createUser()

        repository.create(userId)

        assertFails {
            repository.create(userId)
        }
    }

    @Test
    fun `존재하지 않는 userId로 초대코드를 생성할 수 없다`() {
        assertFails {
            repository.create(999_999u)
        }
    }
}
