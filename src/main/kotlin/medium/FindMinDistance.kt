package medium

import assertEquals

/**
 * Yandex
 * Найти минимальное расстояние между символами X и Y в строке
 * Расстояние - разница индексов по модулю
 * Если нет пары X и Y - вернуть -1
 *
 * Временная сложность: O(n)
 * Пространственная сложность: O(1)
 */
class FindMinDistance {

    fun find(string: String): Int {
        if (string.length < 2) return -1

        var minDistance = Int.MAX_VALUE

        var baseValue: Char? = null
        var baseIndex = -1


        for (i in 0 until string.length) {
            if (string[i] == 'X' || string[i] == 'Y') {
                if (baseValue != null && baseValue != string[i]) {
                    val distance = i - baseIndex
                    if (distance < minDistance) minDistance = distance
                }
                baseValue = string[i]
                baseIndex = i
            }
        }

        return if(minDistance == Int.MAX_VALUE) -1 else minDistance
    }
}

fun main() {
    val test = FindMinDistance()
    assertEquals(-1, test.find("abcd"))
    assertEquals(-1, test.find("aXcd"))
    assertEquals(-1, test.find("aXXd"))
    assertEquals(4, test.find("XbcdY"))
    assertEquals(1, test.find("XXYYcd"))
    assertEquals(1, test.find("XXaYfXY"))

    assertEquals(1, test.find("XXOOYXYXO"))
    assertEquals(-1, test.find("XOXOXO"))
    assertEquals(-1, test.find("XXXXXOOO"))
}