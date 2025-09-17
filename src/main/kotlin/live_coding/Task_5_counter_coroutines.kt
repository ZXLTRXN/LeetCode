@file:Suppress("unused")
package live_coding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger

/**
 * https://github.com/kish-dev/coroutines-interview
 *
 * Что будет выведено?
 * Какими способами можно сделать так, чтобы вывод был 1_000_000?
 */

fun main() {
    runBlocking(Dispatchers.Default) {
//        initialTask()
//        atomicSolution()
        mutexSolution()
    }
}

suspend fun CoroutineScope.initialTask() {
    var counter = 0
    val jobs = List(1_000) {
        launch {
            delay(1000)
            repeat(1_000) {
                counter++
            }
        }
    }
    jobs.joinAll()
    println("counter=$counter")
}

suspend fun CoroutineScope.atomicSolution() {
    val counter = AtomicInteger(0)
    val jobs = List(1_000) {
        launch {
            delay(1000)
            repeat(1_000) {
                counter.addAndGet(1)
            }
        }
    }
    jobs.joinAll()
    println("counter=${counter.get()}")
}

suspend fun CoroutineScope.mutexSolution() {
    val mutex: Mutex = Mutex()
    var counter = 0
    val jobs = List(1_000) {
        launch {
            delay(1000)
            repeat(1_000) {
                mutex.withLock {
                    counter++
                }
            }
        }
    }
    jobs.joinAll()
    println("counter=$counter")
}