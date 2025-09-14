package easy

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PalindromeTest {
    lateinit var p: Palindrome

    @BeforeEach
    fun init() {
        p = Palindrome()
    }

    @Test
    fun `valid palindrome returns true`() {
        assert(p.isPalindrome(".,"))
        assert(p.isPalindrome("a."))
        assert(p.isPalindrome("A man, a plan, a canal: Panama"))
        assert(p.isPalindrome("ab_a"))
        assert(p.isPalindrome(" "))
        assert(p.isPalindrome("aa"))

    }

    @Test
    fun `valid palindrome returns false`() {
        assertFalse(p.isPalindrome("race a car"))


    }
}