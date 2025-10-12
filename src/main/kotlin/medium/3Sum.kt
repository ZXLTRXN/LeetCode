package medium

/**
 * https://leetcode.com/problems/3sum
 * cpu nlogn(сорт отбр) + n^2, mem n тк sorted, а triplets это формат результата его не учитываем
 */
class Sum3 {
    fun threeSum(nums: IntArray): List<List<Int>> {
        if (nums.size < 3) return emptyList()

        val numsSorted = nums.sorted()
        val triplets = mutableSetOf<List<Int>>()

        for (i in numsSorted.indices) {
            var leftIdx = i + 1
            var rightIdx = numsSorted.size - 1


            while (leftIdx < rightIdx) {
                val sum = numsSorted[i] + numsSorted[leftIdx] + numsSorted[rightIdx]
                if (sum == 0) {

                    triplets.add(listOf(numsSorted[i], numsSorted[leftIdx], numsSorted[rightIdx]))

                    // пропуск дубликатов
                    while (leftIdx < rightIdx && numsSorted[rightIdx] == numsSorted[rightIdx - 1]) rightIdx--
                    while (leftIdx < rightIdx && numsSorted[leftIdx] == numsSorted[leftIdx + 1]) leftIdx++
                    rightIdx--
                    leftIdx++
                }
                if (sum > 0) rightIdx--
                if (sum < 0) leftIdx++
            }
        }
        return triplets.toList()
    }
}

fun main() {
    val test = Sum3()
    require(test.threeSum(intArrayOf(0)) == listOf<Int>())
    require(test.threeSum(intArrayOf(0, 0, 0)).size == 1)
    require(test.threeSum(intArrayOf(0, -5, 5)).size == 1)
    require(test.threeSum(intArrayOf(1, 2, 3)).isEmpty())
    require(test.threeSum(intArrayOf(2, 3, -5)).size == 1)
    require(test.threeSum(intArrayOf(-1,0,1,2,-1,-4)).size == 2) // !3 тк 1 дубликат по условию надо отбросить
}