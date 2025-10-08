package easy

/**
 * Дана последовательность неотрицательных целых чисел
 * вычислите наибольшее произведение, при том что i != j, но ai = aj возможно
 */
class MaxMultiplication {

    // test
    // 0 0
    // 0 8
    // 3 3
    // 17 1
    // 1 2 3 100 3 0

    fun solve(values: IntArray): Int {
        var a = 0
        var b = 0

        for (value in values) {
            if (value > a) {
                b = a
                a = value
            } else if (value > b) {
                b = value
            }
        }
        return a*b
    }
}

fun main() {
    val test = MaxMultiplication()
    require(test.solve(intArrayOf(0, 0)) == 0)
    require(test.solve(intArrayOf(0, 8)) == 0)
    require(test.solve(intArrayOf(3, 3)) == 9)
    require(test.solve(intArrayOf(17, 1)) == 17)
    require(test.solve(intArrayOf(1, 2, 3, 100, 3, 0)) == 300)
//    require(test.solve(listOf(12213213123123, 1, )) == 12213213123123) на литкоде Int
}