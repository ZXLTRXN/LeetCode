package easy

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private class UserRepository {
    fun findUser(id: Long): String? = if (id == 1L) "Alice" else null
}

private const val unknownUser = "Unknown user"
private class UserService(private val repository: UserRepository) {
    fun getGreeting(id: Long): String {
        val user = repository.findUser(id) ?: return unknownUser
        return "Hello, $user!"
    }
}

class TestExample {

    private lateinit var a: Anagram
    private val repository: UserRepository = mockk()
    private lateinit var service: UserService

    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            println("⚡ Тестовый класс запускается один раз перед всеми тестами")
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            println("🏁 Все тесты завершены")
        }
    }

    @BeforeEach
    fun setUp() {
        clearMocks(repository)
        service = UserService(repository)
    }

    @AfterEach
    fun tearDown() {
        println("🧹 Тест завершён, можно чистить ресурсы")
    }

    @Nested
    inner class GreetingTests() {

        @Test
        fun `should call repository once`() {
            val slot = slot<Long>()
            every { repository.findUser(capture(slot)) } returns "Alice"
            val id = 1L

            service.getGreeting(id)

            assertEquals(id, slot.captured)
            verify(exactly = 1) { repository.findUser(id) }
        }

        @RepeatedTest(2)
        fun `for unknown user should return unknownUser`() {
            every { repository.findUser(any()) } returns null
            val id = 1L

            val actual = service.getGreeting(id)

            assertEquals(unknownUser, actual)
        }

//        @ParameterizedTest()
        fun `should not throw exceptions on different ids`() {

        }


    }

    fun examples() {
        assert(true)

        assertNotNull("1")

        val exception = assertThrows<RuntimeException>("should throw Runtime") {
            throw RuntimeException("aa")
        }
        assertEquals(expected = "1", actual = exception.message)
    }
}