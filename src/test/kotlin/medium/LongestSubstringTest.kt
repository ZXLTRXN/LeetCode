package medium

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LongestSubstringTest {
    lateinit var tc: LongestSubstring

    @BeforeEach
    fun init() {
        tc = LongestSubstring()
    }

    @Test
    fun valid() {
        Assertions.assertEquals(tc.lengthOfLongestSubstring("abcabcbb"), 3)
        Assertions.assertEquals(tc.lengthOfLongestSubstring("bbbbb"), 1)
        Assertions.assertEquals(tc.lengthOfLongestSubstring("pwwkew"), 3)
        Assertions.assertEquals(tc.lengthOfLongestSubstring("dvdf"), 3)

    }

    @Test
    fun `not valid`() {

    }
}