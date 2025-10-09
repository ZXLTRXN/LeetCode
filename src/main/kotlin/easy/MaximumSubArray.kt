package easy

import kotlin.math.max

/**
 * Given an integer array nums, find the subarray with the largest sum, and return its sum.
 * length >= 1
 * https://leetcode.com/problems/maximum-subarray/description
 */
class MaximumSubArray {
//    fun solve(arr: IntArray): Int { // divide and conqueror не вышло
//        if (arr.size == 1) return arr[0]
//        val mid = arr.size / 2
//
//        val maxLeft = solve(arr.slice(0 until mid).toIntArray())
//        val maxRight = solve(arr.slice(mid until arr.size).toIntArray())
//        val childMax = max(maxRight, maxLeft)
//        return max(maxLeft + maxRight, childMax)
//    }
//
    fun kadaneSolve(arr: IntArray): Int {
        var max = arr[0]
        var curSum = arr[0]
        for (i in 1 until arr.size) {
            curSum = max(arr[i], curSum + arr[i]) // если arr[i] > curSum + arr[i] значит
            // то что было ранее только ухудшает ситуацию и лучше это откинуть
            max = max(curSum, max)
        }
        return max
    }
}

fun main() {
    val test = MaximumSubArray()

    require(test.kadaneSolve(intArrayOf(0)) == 0)
    require(test.kadaneSolve(intArrayOf(0,1,-3)) == 1)
    require(test.kadaneSolve(intArrayOf(10, 2, 1, -3, 12)) == 22)

}