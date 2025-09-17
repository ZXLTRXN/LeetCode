package live_coding

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * https://github.com/kish-dev/coroutines-interview
 *
 * «Что будет выведено?» (чтение + рефакторинг)
 */

fun main() {
    runBlocking {
        println("1")
        println(getW())
        println("2")
    }
}

suspend fun getW(): String = coroutineScope { // упадет, используй supervisor
    val f = async { getF() }
    val t = async { getT() }
    delay(200)
    // t throw до завершения поэтому
    t.invokeOnCompletion { // позволит посмотреть ошибку если есть
        println("t ex $it")
    }
    t.cancel()
    "${f.await()}"
}

suspend fun getF(): String {
    delay(1000)
    return "Sunny"
}

suspend fun getT(): String {
    delay(100)
    throw RuntimeException("T Error")
    return "30 d"
}