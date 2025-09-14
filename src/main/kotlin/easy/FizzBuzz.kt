package easy

// https://leetcode.com/problems/fizz-buzz
@Suppress("unused")
class FizzBuzz {
    fun fizzBuzz(n: Int): List<String> {
        val result: List<String> = List(n) {
            val v = it + 1
            val fizz = v % 3 == 0
            val buzz = v % 5 == 0
            when {
                fizz && buzz -> "FizzBuzz"
                fizz -> "Fizz"
                buzz -> "Buzz"
                else -> v.toString()
            }
        }
        return result
    }
}