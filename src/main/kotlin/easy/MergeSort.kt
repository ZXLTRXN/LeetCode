package easy

class MergeSort {
    // Идея разделить массивы рекурсивно до самого минимального шага в единичку размером, а затем смержить их сортируя
    fun mergeSort(array: IntArray): IntArray {
        if (array.size <= 1) return array

        val mid = array.size / 2

        val left = mergeSort(array.slice(0 until mid).toIntArray())
        val right = mergeSort(array.slice(mid until array.size).toIntArray())

        return merge(left, right)

    }

    fun merge(left: IntArray, right: IntArray): IntArray {
        val result = IntArray(left.size + right.size)
        var r = 0
        var i = 0
        var j = 0

        while (r < result.size && i < left.size && j < right.size) {
            if(left[i] <= right[j]) {
                result[r] = left[i]
                i++
            } else {
                result[r] = right[j]
                j++
            }
            r++
        }

        while (i < left.size) {
            result[r] = left[i]
            i++
            r++
        }

        while (j < right.size) {
            result[r] = right[j]
            j++
            r++
        }

        return result
    }
}

fun main() {
    val res = MergeSort().mergeSort(intArrayOf(1,4,2,1,5,3,1,6,7,3, 12 ,12))
    res.forEach { println(it) }
}