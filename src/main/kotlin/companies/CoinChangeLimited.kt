package companies

import assertEquals

/**
 * Yandex
 * Банкомат.
 * В банкомате есть купюры — 50, 100, 500, 1000, 5000 руб.
 * Номиналы купюр только такие, они не меняются и доступны в константе nominals.
 * Есть ограничение на количество каждой из купюр (аргумент limits).
 * Нужно вернуть купюры и их количество, которыми можно выдать запрашиваемую сумму,
 * в виде объекта в формате, аналогичном объекту лимитов.
 * При прочих равных возможностях выдать одну и ту же сумму разными купюрами
 * приоритет отдаётся крупным.
 *
 * Временная сложность: O(n), где n - количество номиналов купюр
 * Пространственная сложность: O(n)
 *
 * для такой сложности только жадный подходит, у динамического amount*n


Если осталась непокрытая сумма - ошибка "Not enough money"
 */
class CoinChangeLimited {
    fun atm(amount: Int, limits: List<Int>, nominals: List<Int>): Map<Int, Int> {
        if (limits.size != nominals.size) throw IllegalArgumentException("nominal and limits not match by count")

        val results = HashMap<Int, Int>()
        var currentAmount = amount

        for (i in nominals.size - 1 downTo 0) {
            if (limits[i] == 0) continue

            val maxAmountForNominal = currentAmount / nominals[i]
            if (maxAmountForNominal < 1) continue
            val availableAmountForNominal =  if (maxAmountForNominal > limits[i]) { limits[i] } else { maxAmountForNominal }
            currentAmount -= nominals[i] * availableAmountForNominal
            results[nominals[i]] = availableAmountForNominal
        }
        if (currentAmount > 0) throw RuntimeException("Not enough money")
        return results
    }
}

fun main() {
    val test = CoinChangeLimited()

    val nominals = listOf(50, 100, 500, 1000, 5000)
    val limits = listOf(4, 3, 5, 3, 0)

    assertEquals(mapOf(1000 to 1, 100 to 2, 50 to 1),test.atm(1250, limits, nominals))
    assertEquals(mapOf(1000 to 2, 100 to 3, 50 to 2),test.atm(2400, limits, nominals))
    assertEquals(mapOf(1000 to 3, 500 to 5),test.atm(5500, limits, nominals))

    try {
        test.atm(4, limits, nominals)
    } catch (ex: Exception) {
        assertEquals("Not enough money", ex.message)
    }

    try {
        test.atm(51, limits, nominals)
    } catch (ex: Exception) {
        assertEquals("Not enough money", ex.message)
    }

}