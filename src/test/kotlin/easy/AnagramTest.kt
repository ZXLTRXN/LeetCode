package easy

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AnagramTest {

    lateinit var a: Anagram

    @BeforeEach
    fun init() {
        a = Anagram()
    }

    @Test
    fun `correct anagram returns true`() {
        assert(a.solve("anagram", "nagaram"))
        assert(a.solve("abcd", "dabc"))
        assert(a.solve("a", "a"))
    }

    @Test
    fun `not anagram returns false`() {
        assertFalse(a.solve("rat", "car"))
        assertFalse(a.solve("abcd", "dabca"))
        assertFalse(a.solve("", ""))
    }
}