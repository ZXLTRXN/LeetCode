package medium


// https://www.codewars.com/kata/55983863da40caa2c900004e
class NextBiggerSameDigits {
    fun nextBiggerNumber(n: Long): Long {
        val s = n.toString().toCharArray()

        // Шаг 1: Ищем pivot (опору) — первый элемент с конца, который МЕНЬШЕ следующего за ним
        var pivotIdx = -1
        for (i in s.lastIndex - 1 downTo 0) {
            if (s[i] < s[i + 1]) {
                pivotIdx = i
                break
            }
        }

        // Если такого элемента нет, значит все цифры идут по убыванию (например, 54321).
        // Сделать большее число невозможно.
        if (pivotIdx == -1) return -1

        // Шаг 2: Ищем с конца самую МАЛЕНЬКУЮ цифру, которая при этом БОЛЬШЕ, чем s[pivotIdx]
        var swapIdx = s.lastIndex
        for (i in s.lastIndex downTo pivotIdx + 1) {
            if (s[i] > s[pivotIdx]) {
                swapIdx = i
                break
            }
        }

        // Шаг 3: Меняем местами pivot и найденную цифру
        val temp = s[pivotIdx]
        s[pivotIdx] = s[swapIdx]
        s[swapIdx] = temp

        // Шаг 4: Сортируем все цифры ПОСЛЕ pivotIdx по возрастанию
        // Так как они гарантированно шли по убыванию, достаточно их просто развернуть (reverse)
        s.sort(pivotIdx + 1, s.size)

        val result = String(s).toLong()
        return if (result > n) result else -1
    }



}