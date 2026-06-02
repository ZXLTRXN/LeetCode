package medium

// https://www.codewars.com/kata/5672682212c8ecf83e000050
class TwiceLinear {

    fun dblLinear(n: Int): Int {
        var yIndex = 0
        var zIndex = 0
        var i = 1
        val u = Array(n + 1) { 1 }

        while ( i <= n) {
            val y = 2 * u[yIndex] + 1
            val z = 3 * u[zIndex] + 1
            if (y < z) {
                u[i] = y
                yIndex++
            } else if (y == z) {
                u[i] = y
                yIndex++
                zIndex++
            } else {
                u[i] = z
                zIndex++
            }
            i++
        }
        return u[n]
    }

}