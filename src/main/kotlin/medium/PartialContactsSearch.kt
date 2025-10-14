package medium

import assertEquals
import kotlin.math.max

/**
 * Yandex
 * Мы хотим вызвать такси своему другу.
 * Для этого нужно реализовать нечёткий поиск по списку контактов пользователя.
 *
 * Условия задачи:
 *
 *     Входные данные: строка поиска (input) и список контактов (dataSet).
 *     Нужно найти в списке контактов такие строки, которые максимально похожи на строку поиска.
 *     Похожесть определяется на основе частичного совпадения символов и подстрок (нечёткий поиск).
 *
 * Примеры:
 *
 *     Контакты: "John Smith", "Mike Marley", "Hillary Cosplay", "Mark Johnson"
 *     Ищем: "Joh"
 *     Результат: ["John Smith", "Mark Johnson"]
 *
 *     Контакты: "John Smith", "Mike Marley", "Hillary Cosplay", "Mark Johnson"
 *     Ищем: "m John"
 *     Результат: ["Mark Johnson"]
 *
 *     Контакты: "John Smith", "Mike Marley", "Hillary Cosplay", "Mark Johnson", "Kamil Englo", "Mjohn Kengsman", "Mjohn Keng"
 *     Ищем: "keng"
 *     Результат: ["Kamil Englo", "Mjohn Kengsman", "Mjohn Keng"]
 *
 * LCS Table — это двумерная таблица dp,
 * где dp[i][j] хранит длину наибольшей общей подпоследовательности(количество совпавших букв)
 * для первых i символов строки A и первых j символов строки B.
 *
 * LCS отвечает на вопрос:
 *
 * “Какую самую длинную часть первой строки
 * можно получить, если вычеркнуть из второй какие-то символы,
 * но не менять их порядок?”
 *
 */
class PartialContactsSearch {
    fun find(input: String, dataSet: Set<String>): List<String> {
        if (input.isBlank()) return emptyList()
        val results = mutableListOf<String>()

        for (name in dataSet) {
            if (input.length > name.length) continue

            val longestMatching = longestMatching(name.lowercase(), input.lowercase().trim())

            if (longestMatching == input.length) results.add(name)

            // lcsSimilarity additional filter
        }
        return results
    }

    // заполнение таблицы с помощью дин программ, дает нам длину самой длинной подпоследовательности
    fun longestMatching(a: String, b: String): Int {
        val matchTable = Array(a.length + 1) {
            IntArray(b.length + 1) // { 0 }
        }

        for (i in 1 until a.length + 1) { // 0 строка и столбец заполненный нулями нужен чтобы не выйти за границы
            for (j in 1 until b.length + 1) {
                if (a[i - 1] == b [j - 1]) {
                    matchTable[i][j] = matchTable[i - 1][j - 1] + 1 // кол-во совпадений в (строке длиной на 1 меньше) + 1
                } else {
                    matchTable[i][j] = max(
                        matchTable[i][j - 1], // если мы не используем последний символ b
                        matchTable[i - 1][j] // если мы не используем последний символ a
                        // Если бы мы не брали максимум, то каждая несовпадающая пара
                        //сбрасывала бы длину обратно в 0 — и всё рушилось бы.
                    )
                }
            }
        }
        return matchTable[a.length][b.length]
    }

    fun lcsSimilarity(a: String, b: String): Double {
        val lcs = longestMatching(a, b)
        val maxLen = maxOf(a.length, b.length)
        return lcs.toDouble() / maxLen
    }
}

fun main() {
    val test = PartialContactsSearch()
    assertEquals(listOf("John Smith", "Mark Johnson"),
        test.find("Joh", setOf("John Smith", "Mike Marley", "Hillary Cosplay", "Mark Johnson"))
    )
    assertEquals(listOf("Mark Johnson"),
        test.find("m John", setOf("John Smith", "Mike Marley", "Hillary Cosplay", "Mark Johnson"))
    )
    assertEquals(listOf("Kamil Englo", "Mjohn Kengsman", "Mjohn Keng"),
        test.find("keng", setOf("John Smith", "Mike Marley", "Hillary Cosplay",
            "Mark Johnson", "Kamil Englo", "Mjohn Kengsman", "Mjohn Keng"))
    )

}