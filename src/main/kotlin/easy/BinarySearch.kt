package easy
class BinarySearch {

    /**
        @param nums - sorted ascending
     **/
    fun search(nums: IntArray, target: Int): Int {
        return cycleBinarySearch(nums, target)
    }

    private fun cycleBinarySearch(nums: IntArray, target: Int): Int {
        var start = 0
        var end = nums.size - 1
        var middle: Int
        var found = false

        while (!found && start <= end) {
            middle = (start + end) / 2
            val value = nums[middle]
            found = value == target
            if (found) return middle
            if (value > target) end = middle - 1
            else start = middle + 1
        }
        return -1
    }

    private fun recursiveBinarySearch(nums: IntArray, target: Int, start: Int, end: Int): Int {
        if (start > end) return -1
        val middle: Int = (start + end) / 2
        if (nums[middle] == target) return middle

        return if (nums[middle] > target) recursiveBinarySearch(nums, target, 0, middle - 1)
        else recursiveBinarySearch(nums, target, middle + 1, end)
    }
}