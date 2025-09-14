package easy

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LongestSubstringTest {
    lateinit var tc: LongestSubstring

    @BeforeEach
    fun init() {
        tc = LongestSubstring()
    }

    @Test
    fun `valid`() {
        assertEquals(tc.lengthOfLongestSubstring("abcabcbb"), 3)
        assertEquals(tc.lengthOfLongestSubstring("bbbbb"), 1)
        assertEquals(tc.lengthOfLongestSubstring("pwwkew"), 3)
        assertEquals(tc.lengthOfLongestSubstring("dvdf"), 3)

    }

    @Test
    fun `not valid`() {

    }
}