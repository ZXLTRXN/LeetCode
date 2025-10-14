package medium

import assertEquals


/**
 * https://leetcode.com/problems/permutation-in-string
 * тут надо ездяшим окном решать, окно движется добавляя элемент справа, и выкидывая из мапы элемент слева
 */
class PermutationInString {

//    fun checkInclusion(s1: String, s2: String): Boolean {
//        if(s2.length > s1.length || s1.isEmpty()) return false
//        val s1Letters = frequencyMap(s1)
//
//        val windowSize = s1.length
//
//
//        for (idx in s2.indices) {
//            if (idx + windowSize - 1 > s2.lastIndex) break
//
//            val s2Letters = frequencyMap(s2.substring(idx, idx + windowSize))
//
//            if (s1Letters == s2Letters) return true
//        }
//
//        return false
//    }
//
//    fun frequencyMap(string: String): Map<Char, Int> {
//        val letters = HashMap<Char, Int>()
//
//        for (l in string) {
//            letters.compute(l) { _, value ->
//                if (value == null) 1 else value + 1
//            }
//        }
//        return letters
//    }

}

fun main() {
    val test = PermutationInString()
//    assertEquals(true, test.checkInclusion("ab", "aasfbae"))
//    assertEquals(true, test.checkInclusion("a", "a"))
//    assertEquals(false, test.checkInclusion("vb", "afwf"))
//    assertEquals(false, test.checkInclusion("vbaa", "vba"))
//    assertEquals(false, test.checkInclusion("", "vba"))
//    assertEquals(true, test.checkInclusion("adc", "dcda"))
//    assertEquals(false, test.checkInclusion("hello", "ooolleoooleh"))
}