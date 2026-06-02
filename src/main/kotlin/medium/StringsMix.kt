package medium

import java.util.TreeMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.max

// https://www.codewars.com/kata/5629db57620258aa9d000014
data class Counter(val s1: Int, val s2:Int) {
    val max = max(s1, s2)
    val maxString = if (s1 > s2) "1" else if (s1 == s2) "=" else "2"
}

class StringsMix {

    val map = TreeMap<Char, Counter>()
    fun sort() {
        val resultString = map.entries
            .filter { it.value.max > 1 }
            .sortedWith(
                compareByDescending<Map.Entry<Char, Counter>> { it.value.max }
                    .thenBy { it.value.maxString }
                    .thenBy { it.key }
            )
            // Превращаем каждую запись в строку нужного формата "X:yyyy"
            .joinToString(separator = "/") { (char, counter) ->
                "${counter.maxString}:${char.toString().repeat(counter.max)}"
            }
    }
}