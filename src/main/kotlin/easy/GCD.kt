package easy

import kotlin.math.max

class GCD {
    fun solve(value1: Int, value2: Int): Int {
        var a = value1
        var b = value2
        while (a > 0 && b > 0) {
            if (a >= b) {
                a = a % b
            } else {
                b = b % a
            }
        }
        return max(a,b)
    }
}