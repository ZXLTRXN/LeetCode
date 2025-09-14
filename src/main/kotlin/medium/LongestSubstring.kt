package medium

import kotlin.math.max
import kotlin.text.iterator

class LongestSubstring {

    fun lengthOfLongestSubstring(s: String): Int {
        var max = 0
        var startIndex = 0
        val set: MutableSet<Char> = mutableSetOf()

        for (l in s) {
            if (set.contains(l)) {
                max = max(set.size, max)
                do {
                    val letterToDrop = s[startIndex]
                    set.remove(letterToDrop)
                    startIndex++
                } while (set.contains(l))
            }
            set.add(l)
        }

        return max(set.size, max)
    }
}