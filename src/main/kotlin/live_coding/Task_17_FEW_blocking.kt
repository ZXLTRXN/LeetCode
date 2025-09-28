package live_coding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun alphaBlock() {
    val dispatcher = Dispatchers.Default.limitedParallelism(1)
    runBlocking(dispatcher) {
        launch {
            repeat(10) {
                println("$it")
                delay(100)
            }
        }
        launch {
            runBlocking {
                println("starting")
                delay(100 * 10)
                println("finfished")
            }
        }
    }
}
// 0 starting finished 1 2 3...

// еще одна похожая
fun ozonBlock() {
    runBlocking {
        val a = launch {
            while (true) { // isActive
                Thread.sleep(1000)
                println("Show Loading")
            }
        }

        launch {
            println("a")
            withContext(Dispatchers.IO) {
                println("b")
                delay(5500)
                a.cancel() // очевидно не сработает
                println("Finished")
            }
        }
        println("c")
    }
} // a, b, c добавил я,
// a и b не выведется, тк 2ой launch стартует тоже на мейне, а он заблочен первой корутиной,
// то есть мы даже не дойдем до переключения контекста

fun main() {
    ozonBlock()
}
