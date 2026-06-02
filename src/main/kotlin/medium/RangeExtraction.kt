package medium

// https://www.codewars.com/kata/51ba717bb08c1cd60f0
class RangeExtraction {
    fun rangeExtraction(arr: IntArray): String {
        val result = StringBuilder()
        var startIdx = 0
        var endIdx = 1

        while (endIdx <= arr.lastIndex) {
            if (arr[endIdx] != arr[startIdx] + (endIdx - startIdx)) {
                appendBlock(result, arr, startIdx, endIdx - 1)
                startIdx = endIdx
            }
            endIdx++
        }

        appendBlock(result, arr, startIdx, endIdx - 1)

        return result.toString()
    }

    fun appendBlock(sb: StringBuilder, arr: IntArray, start: Int, end: Int) {
        if (sb.isNotEmpty()) {
            sb.append(",")
        }

        val length = end - start + 1
        when {
            length >= 3 -> sb.append("${arr[start]}-${arr[end]}")
            length == 2 -> sb.append("${arr[start]},${arr[end]}")
            else -> sb.append("${arr[start]}")
        }
    }
}