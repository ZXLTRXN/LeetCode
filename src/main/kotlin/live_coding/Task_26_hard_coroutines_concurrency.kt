package live_coding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread
import kotlin.time.Duration.Companion.nanoseconds

/**
 * Дана функция main(), в которой выполняется некоторый код.
 * Расскажите, что конкретно в нем происходит и что будет выведено
 * в консоли после выполнения данного кода
 */

private class Counter(var count: Long) { // var зачем
    fun increment() {
        count++ // не атомарно
    }

    fun decrement() {
        count--
    }
}

private const val PIECES_OF_WORK = 1000

private var counterOfCompletedPiecesOfWork = Counter(0)

// тут не заработает нормально, надо на android
fun main() {
    val th = thread {
//        Handler(Looper.getMainLooper()).post { // android
                MainScope().launch { // android, не хватает exHandler для перехвата от Async
                coroutineScope {
                    launch {
                        withContext(Dispatchers.Default) {
                            (1..PIECES_OF_WORK).map {
                                async {
                                    delay(2.nanoseconds) // simulate work time
                                    counterOfCompletedPiecesOfWork.increment()
                                }
                            }.awaitAll()
                        }
                    }
                }
                try {
                    async {
                        delay(10.nanoseconds)
                        when ((0..100).random()) {
                            else -> throw Exception("Failed to do simple work")
                        }
                    }
                } catch (e: Exception) {
                    println(e)
                }
                runBlocking(Dispatchers.Main) { // android , заблочит поток
                    for (i in 1..PIECES_OF_WORK) {
                        delay(2.nanoseconds) // simulate work time
                        counterOfCompletedPiecesOfWork.decrement()
                    }
                    println(counterOfCompletedPiecesOfWork) // не переопределен toString
                }
            }
//        }
    }
    th.join()
}