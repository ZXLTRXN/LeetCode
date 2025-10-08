package easy

/**
 * https://leetcode.com/problems/non-overlapping-intervals
 * Чтобы максимизировать число непересекающихся интервалов (и, соответственно, минимизировать удаление),
 * нужно всегда выбирать интервал, который заканчивается раньше всех — тогда он оставляет больше пространства для последующих.
 * Реализуем жадный алгоритм
 */
class NonOverlappingIntervals {
    fun solve(intervals: Array<IntArray>): Int {
        if (intervals.size < 2) return 0

        intervals.sortBy { it[1] }

        val resultIntervals: MutableList<IntArray> = mutableListOf() // чисто для наглядности
        var needRemove = 0

        var prevIndex = 0
        var nextIndex = 1

        resultIntervals.add(intervals[prevIndex]) // с самой близкой к началу правой границей пойдет точно в результирующий список

        while (nextIndex < intervals.size) {
            val prevInterval = intervals[prevIndex]
            val nextInterval = intervals[nextIndex]

            if (!isOverlapped(prevInterval, nextInterval)) {
                resultIntervals.add((nextInterval))
                prevIndex = nextIndex
            } else {
                needRemove++
            }
            nextIndex++
        }

        println("resultIntervals")
        resultIntervals.forEach {
            println("[${it[0]}, ${it[1]}]")
        }

        return needRemove
    }

    fun isOverlapped(interval1: IntArray, interval2: IntArray): Boolean  {
        val leftBorder1 = interval1[0]
        val rightBorder1 = interval1[1]

        val leftBorder2 = interval2[0]
        val rightBorder2 = interval2[1]

        return (rightBorder2 > leftBorder1 && rightBorder1 > leftBorder2) ||
                leftBorder1 == leftBorder2 ||
                rightBorder1 == rightBorder2
    }

}

fun main() {
    val test = NonOverlappingIntervals()

    require(test.solve(arrayOf(intArrayOf(1,2), intArrayOf(2,3), intArrayOf(3,4), intArrayOf(1,3))) == 1)
    require(test.solve(arrayOf(intArrayOf(1,2), intArrayOf(1,2), intArrayOf(1,2))) == 2)
    require(test.solve(arrayOf(intArrayOf(1,2), intArrayOf(2,3))) == 0)
}