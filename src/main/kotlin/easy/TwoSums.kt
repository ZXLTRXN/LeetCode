package easy

class TwoSums {
    fun twoSum(nums: IntArray, target: Int): IntArray {
        for (i in nums.indices) {
            for (j in (i+1)..<nums.size) {
                if (nums[i] + nums[j] == target) return intArrayOf(i, j)
            }
        }
        return intArrayOf()
    }

    fun twoSumOptimal(nums: IntArray, target: Int): IntArray {
        val subtractionMap = HashMap<Int, Int>()
        for (i in nums.indices) {
            val subtraction = target - nums[i]
            if(subtractionMap.containsKey(nums[i])) return intArrayOf(subtractionMap[nums[i]]!!, i)
            else subtractionMap[subtraction] = i
        }
        return intArrayOf()
    }
}