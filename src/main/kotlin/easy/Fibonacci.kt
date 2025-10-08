package easy

// Задачи связанные с Фибоначчи
class Fibonacci {
    // test
    // 0, 1, 6

    private val values: MutableMap<Int, Int> = HashMap()

    // recursive memo mem o(n) cpu o(n)
    fun fib(n: Int): Int {
        if (n < 0) throw IllegalArgumentException()
        if (n <= 1) return n
        return if (values.contains(n)) {
            values[n]!!
        } else {
            val newValue = fib(n-2) + fib(n-1)
            values[n] = newValue
            newValue
        }
    }

    // iterative memo o(1) cpu n
    fun fibIter(n: Int): Int {
        if (n <= 1) return n
        var l0 = 0 // l-2
        var l1 = 1 // l-1
        var res = 0
        var i = 1 // число которое вычислено в данный момент
        while (i < n) {
            i++
            res = l0 + l1
//            println("i = $i res = $res lo = $l0 l1 = $l1")
            l0 = l1
            l1 = res

        }
        return res
    }

    // надо найти последнюю цифру для Fn
    // последняя цифра может быть вычислена, если брать по модулю 10 каждое вычисленное в последовательности
    // F102 = 6 F103 = 7 F104 = 3, то есть сумме предыдущих по модулю 10,
    // таким образом важно что мы сохраняем для вычислений последнюю цифру,
    // остальное что может привести к переполнению нам не важно
    fun fibLastDigit(n: Int): Int {
        if (n <= 1) return n
        var l0 = 0 // l-2
        var l1 = 1 // l-1
        var res = 0
        var i = 1 // число которое вычислено в данный момент
        while (i < n) {
            i++
            res = (l0 + l1) % 10
            l0 = l1
            l1 = res

        }
        return res
    }

    // надо найти Fn mod m
    // если выписать значения для модулей например 2 или 3, то можно увидеть период
    // тогда задача сводится к нахождению периода Пизано (начинается c 01 всегда)
    // хер его знает как это делать
    fun fibMod(n: Int, m: Int) {
    }
}

fun main() {
    val test = Fibonacci()
// get last digit

    for (n in 25..36) {
        println("n= $n value = ${test.fibLastDigit(n)}")
    }

// get value
//    require(test.fibIter(0) == 0)
//    require(test.fibIter(1) == 1)
//    require(test.fibIter(2) == 1)
//    require(test.fibIter(3) == 2)
//    require(test.fibIter(4) == 3)
}