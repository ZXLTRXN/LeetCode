package medium

import assertEquals

/**
 * https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii
 */
class RemoveDuplicatesSorted2 {
    fun removeDuplicates(nums: IntArray): Int {
        if (nums.isEmpty()) return 0

        var currentValueCount = 0
        var currentValue = nums[0]
        var removedValues = 0


        for (i in nums.indices) {
            if(currentValue == nums[i]) currentValueCount++ else {
                currentValueCount = 1
                currentValue = nums[i]
            }

            if (currentValueCount > 2) {
                nums[i] = Int.MIN_VALUE
                removedValues += 1
            } else if (removedValues > 0) {
                nums[i - removedValues] = nums[i]
                nums[i] = Int.MIN_VALUE
            }

        }
        return nums.size - removedValues
    }
}


fun main() {
    val test = RemoveDuplicatesSorted2()
    assertEquals(test.removeDuplicates(intArrayOf(1)), 1)
    assertEquals(test.removeDuplicates(intArrayOf(1, 1, 1, 1)), 2)

    val arr = intArrayOf(1,1,1,1,2,3,4,4,5,6,6,6)
    assertEquals(test.removeDuplicates(arr), 9)
    val expectedArr = intArrayOf(1,1,2,3,4,4,5,6,6, Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
    arr.forEach { value ->
        print(value)
    }
    println()

    arr.forEachIndexed { index, value  ->
        print(value)
        assertEquals(expectedArr[index], value, "arr element with index $index")
    }
}