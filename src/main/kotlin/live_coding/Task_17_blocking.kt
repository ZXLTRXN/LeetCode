package live_coding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

//fun main() {
//    val dispatcher = Dispatchers.Default.limitedParallelism(1)
//    runBlocking(dispatcher) {
//        launch {
//            repeat(10) {
//                println("$it")
//                delay(100)
//            }
//        }
//        launch {
//            runBlocking {
//                println("starting")
//                delay(100 * 10)
//                println("finfished")
//            }
//        }
//    }
//}
// 0 starting finished 1 2 3...

fun main() {
    runBlocking {
        val a = launch {
            while (true) { // isActive
                Thread.sleep(1000)
                println("Show Loading")
            }
        }

        launch {
            withContext(Dispatchers.IO) {
                delay(5500)
                a.cancel() // очевидно не сработает
                println("Finished") // полагаю не выводится т к 1ый лонч блочит вывод
            }
        }
    }
}