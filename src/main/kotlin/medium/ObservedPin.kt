package medium

// https://www.codewars.com/kata/5263c6999e0f40dee200059d
class ObservedPin {

    fun getPINs(observed: String): List<String> {
        return observed.fold(listOf("")) { acc, digit ->
            val adjs = getAdjacent(digit)
            acc.flatMap { prefix ->
                adjs.map {
                    prefix + it
                }
            }
        }
    }


    fun getAdjacent(digit: Char): List<Char> {
        return when (digit) {
            '0' -> listOf('0', '8')
            '1' -> listOf('1', '2', '4')
            '2' -> listOf('1', '2', '3', '5')
            '3' -> listOf('2', '3', '6')
            '4' -> listOf('1', '4', '5', '7')
            '5' -> listOf('2', '4', '5', '6', '8')
            '6' -> listOf('3', '5', '6', '9')
            '7' -> listOf('4', '7', '8')
            '8' -> listOf('0', '5', '7', '8', '9')
            '9' -> listOf('6', '8', '9')
            else -> throw IllegalArgumentException("Wong char")
        }
    }
}

