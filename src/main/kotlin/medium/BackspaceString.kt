package medium

import assertEquals

/**
 * Проверить равны ли строки с учетом операций backspace (#)
 * Без использования дополнительной памяти
 *
 * Временная сложность: O(n), где n - максимальная длина строк
 * Пространственная сложность: O(1)
 */
class BackspaceString {
    fun compare(a: String, b: String): Boolean {
        var ignoreCountA = 0
        var ignoreCountB = 0

        var aIndex = a.lastIndex
        var bIndex = b.lastIndex
        while (aIndex >= 0 && bIndex >= 0) {
            while (aIndex >= 0 && (a[aIndex] == '#' || ignoreCountA > 0) ) {
                if (a[aIndex] == '#') {
                    ignoreCountA++
                } else {
                    ignoreCountA--
                }
                aIndex--
            }

            while (bIndex >= 0 && (b[bIndex] == '#' || ignoreCountB > 0)) {
                if (b[bIndex] == '#') {
                    ignoreCountB++
                } else {
                    ignoreCountB--
                }
                bIndex--
            }

            if (aIndex < 0 && bIndex < 0) {
                return true
            }

            if (aIndex < 0 || bIndex < 0 || a[aIndex] != b[bIndex]) {
                return false
            }
            aIndex--
            bIndex--
        }
        return true
    }
}

fun main() {
    val test = BackspaceString()
    assertEquals(true, test.compare("", ""))
    assertEquals(false, test.compare("###", "a#"))
    assertEquals(true, test.compare("abc", "abc"))
    assertEquals(false, test.compare("abc", "abv"))
    assertEquals(true, test.compare("aa#bb#cc#", "abc"))
    assertEquals(false, test.compare("aa#bb#cc#", "a#b#cab"))
    assertEquals(false, test.compare("abc###v", "a#b#c#v"))

    assertEquals(true, test.compare("ab#c", "ad#c"))
    assertEquals(true, test.compare("ab##", "c#d#"))
    assertEquals(false, test.compare("a#c", "b"))
    assertEquals(true, test.compare("######abc", "#abc"))
}
