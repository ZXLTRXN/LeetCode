package easy

class Palindrome {

    fun isPalindrome(s: String): Boolean {
        val last = s.length - 1
        var i = 0
        var j = last
        while (i < j) {
            while (!letterAlphanumeric(s[i]) && i < j) {
                i++
            }
            while (!letterAlphanumeric(s[j]) && i < j) {
                j--
            }
            if (s[i].lowercase() != s[j].lowercase()) return false
            i++
            j--
        }
        return true
    }

    private fun letterAlphanumeric(c: Char): Boolean {
        return Regex("[a-zA-Z0-9]").matches(c.toString())
    }
}