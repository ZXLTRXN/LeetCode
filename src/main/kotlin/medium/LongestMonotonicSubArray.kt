package medium

import assertEquals

/**
 * Yandex
 * Найти монотонный подотрезок максимальной длины и вернуть индексы его начала и конца.
 * Подотрезок должен быть строго убывающим или строго возрастающим.
 *
 * Временная сложность: O(n)
 * Пространственная сложность: O(1)
 */
class LongestMonotonicSubArray {
    fun findIndices(array: IntArray): Pair<Int, Int> {

        if (array.size < 2) return Pair(0,0)
        if (array.size == 2) {
            return if (array[0] == array[1]) Pair(0,0) else Pair(0,1)
        }

        var isUp = Trend.INIT
        var start = 0
        var end = 1
        var maxStart = 0
        var maxEnd = 0

        while (end < array.size && start < array.size) {
            if (array[end - 1] == array[end]) {
                start = end
                end = start + 1
                isUp = Trend.INIT
                continue
            }

            val nextIsUp = if (array[end - 1] > array[end]) Trend.DOWN else Trend.UP
            if (nextIsUp == isUp || isUp == Trend.INIT) {
                isUp = nextIsUp
                if (maxEnd - maxStart < end - start) {
                    maxEnd = end // 1
                    maxStart = start // 0
                }
            } else {
                isUp = nextIsUp
                start = end - 1
            }
            end++
        }

        return Pair(maxStart, maxEnd)
    }

    enum class Trend {
        UP, DOWN, INIT
    }
}

fun main() {
    val test = LongestMonotonicSubArray()
    assertEquals(Pair(0,0), test.findIndices(intArrayOf()))
    assertEquals(Pair(0,0), test.findIndices(intArrayOf(1)))
    assertEquals(Pair(0,0), test.findIndices(intArrayOf(1, 1)))
    assertEquals(Pair(0,1), test.findIndices(intArrayOf(1, 2, 1)))
    assertEquals(Pair(0,3), test.findIndices(intArrayOf(1, 2, 3, 4)))
    assertEquals(Pair(1,3), test.findIndices(intArrayOf(1, 1, -3, -4, 5)))

    assertEquals(Pair(1,3), test.findIndices(intArrayOf(2, 7, 5, 4, 4, 3)))
    assertEquals(Pair(0,4), test.findIndices(intArrayOf(5, 4, 3, 2, 1, 2)))

}