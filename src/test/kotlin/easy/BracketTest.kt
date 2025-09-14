package easy

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BracketTest {
    lateinit var b: Brackets

    @BeforeEach
    fun init() {
        b = Brackets()
    }

    @Test
    fun `valid brackets returns true`() {
        assert(b.isValid("()[]{}"))
        assert(b.isValid("()"))
        assert(b.isValid("{({}[])}"))

    }

    @Test
    fun `valid brackets returns false`() {
        assertFalse(b.isValid("(]"))
        assertFalse(b.isValid("{"))
        assertFalse(b.isValid("[(])"))
    }
}