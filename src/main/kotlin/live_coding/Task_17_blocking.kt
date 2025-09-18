package live_coding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
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