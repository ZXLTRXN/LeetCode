package medium

import java.util.LinkedList
import java.util.Queue
import kotlin.math.min

/**
 * https://leetcode.com/problems/coin-change

 */
class CoinChange {

    // найти количество монет
    // жадный алгоритм не всегда дает решение, так и тут для последнего примера не найдет
    // cpu sort(n logn) + n(отбрасываем), mem n
    fun coinChange(coins: IntArray, amount: Int): Int {

        var left = amount
        val sortedCoins = coins.sorted()
        var result = 0

        for (i in sortedCoins.size-1 downTo 0) {
            val nominal = sortedCoins[i]
            if (left == 0) break
            val amountOfCoins = left / nominal
            if (amountOfCoins >= 1) {
                left -= amountOfCoins * nominal
                result += amountOfCoins
            }
        }
        return if (left != 0) -1 else result
    }

    /**
     * пытался реализовать динамическую версию, но получился BFS. Ключевое отличие в том,
     * что я не стал проходить по всем состояния, а выбрал только достижимые, ну и
     * ещё очередь пришлось использовать получается по памяти чуть больше
     * cpu amount*n, mem amount + queue(отбрасываем)
     */
    fun coinChangeBFS(coins: IntArray, amount: Int): Int {
        if (amount == 0) return 0

        val map: MutableMap<Int,Int> = HashMap()
        val nextKeys: Queue<Int> = LinkedList()

        for (nominal in coins) {
            if (nominal > amount) continue
            map[nominal] = 1
            nextKeys.add(nominal)
        }

        while (true) {
            val currentAmount: Int = nextKeys.poll() ?: break
            if (currentAmount == amount) break

            for (nominal in coins) {
                val nextAmount = currentAmount + nominal
                if (nextAmount > amount) continue
                val alreadyProcessed = map[nextAmount] != null
                map[nextAmount] = min(map[currentAmount]!! + 1, map[nextAmount] ?: Int.MAX_VALUE)
                if (!alreadyProcessed) nextKeys.add(nextAmount)
            }
        }

        return map[amount] ?: -1
    }

    /** dynamic variant проходится абсолютно по всем вариантам беря данные сзади
     * cpu amount*n, mem amount
     * тоже что и у верхнего, однако тут нет беготни по структурам данных
     * мы просто последовательно работаем с массивом, что френдли для кеша,
     * поэтому сильно быстрее на практике
     */
    fun coinChangeDynamic(coins: IntArray, amount: Int): Int {
        if (amount == 0) return 0
        val coinsNeeded = IntArray(amount + 1) { Int.MAX_VALUE }
        coinsNeeded[0] = 0

        for (currentAmount in 1..amount) {
            for (coin in coins) {
                if (currentAmount - coin >= 0 && coinsNeeded[currentAmount - coin] != Int.MAX_VALUE) {
                    coinsNeeded[currentAmount] = min(coinsNeeded[currentAmount],
                        coinsNeeded[currentAmount - coin] + 1)
                }
            }
        }

        return if (coinsNeeded[amount] == Int.MAX_VALUE) -1 else coinsNeeded[amount]
    }
}

fun main() {
    val test = CoinChange()

    require(test.coinChangeDynamic(intArrayOf(), 5) == -1)
    require(test.coinChangeDynamic(intArrayOf(10), 7) == -1)
    require(test.coinChangeDynamic(intArrayOf(5), 5) == 1)
    require(test.coinChangeDynamic(intArrayOf(5, 1), 7) == 3)
    require(test.coinChangeDynamic(intArrayOf(186, 419, 83, 408), 6249) == 20)
}