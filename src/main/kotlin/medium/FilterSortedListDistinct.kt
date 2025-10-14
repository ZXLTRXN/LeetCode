package medium

import assertEquals

/**
 * Yandex
 * Найти элементы из первой отсортированной последовательности,
 * которых нет во второй отсортированной последовательности.
 *
 * Временная сложность: O(n+m), где n и m - длины последовательностей
 * Пространственная сложность: O(1), не считая результирующий список

 */
class FilterSortedListDistinct {
    fun filter(findIn: List<Int>, exclude: List<Int>): List<Int> {
        var i = 0
        var j = 0
        val result = mutableListOf<Int>()

        while (i < findIn.size && j < exclude.size) {
            when {
                findIn[i] < exclude[j] -> {
                    result.add(findIn[i])
                    i++
                }
                findIn[i] > exclude[j] -> j++
                else -> i++ // равны, пропускаем
            }
        }

        while (i < findIn.size) {
            result.add(findIn[i])
            i++
        }
        return result
    }
}

fun main() {
    val test = FilterSortedListDistinct()
    assertEquals(listOf<Int>(), test.filter(listOf(1, 2), listOf(1, 2)))
    assertEquals(listOf(1, 2, 3, 4), test.filter(listOf(1, 2, 3, 4), listOf(5, 6)))
    assertEquals(listOf(1, 2, 2, 2, 3, 4), test.filter(listOf(1, 2, 2, 2, 3, 4), listOf(5, 6)))
    assertEquals(listOf(1, 2, 4), test.filter(listOf(1, 2, 3, 3, 3, 4), listOf(3)))
    assertEquals(listOf(1, 2), test.filter(listOf(1, 2, 3), listOf(3, 4)))
}