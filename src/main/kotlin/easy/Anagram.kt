package easy

import kotlin.text.iterator

class Anagram() {
    fun solve(s: String, t: String): Boolean {
        if(s.isEmpty() || t.isEmpty() || s.length != t.length) return false

        val map: MutableMap<Char, Int> = HashMap()

        for(ch in s) {
            map.compute(ch) { _, curValue ->
                if (curValue == null) 1
                else curValue + 1
            }
        }

        for(ch in t) {
            val count = map[ch]
            if (count == null || count == 0) return false
            map[ch] = count - 1
        }

        return true
    }
}