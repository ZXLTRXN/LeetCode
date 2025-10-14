package medium

import kotlin.math.min

/**
 * https://leetcode.com/problems/container-with-most-water
 * решаем 2 индексами
 * cpu n, mem 1
 */
class ContainerWithMostWater {
    fun maxArea(height: IntArray): Int {
        var leftIndex = 0
        var rightIndex = height.size - 1
        var maxArea = 0

        while (leftIndex < rightIndex) {
            val area = (rightIndex - leftIndex) * min(height[leftIndex], height[rightIndex])
            if (area > maxArea) maxArea = area
            if (height[leftIndex] > height[rightIndex]) rightIndex-- else leftIndex++
        }

        return maxArea
    }
}

fun main() {
    val test = ContainerWithMostWater()
    require(test.maxArea(intArrayOf(1, 0)) == 0)
    require(test.maxArea(intArrayOf(5)) == 0)
    require(test.maxArea(intArrayOf(5, 1, 4, 3, 2, 8)) == 25)
}