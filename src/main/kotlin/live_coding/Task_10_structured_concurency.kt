package live_coding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Что выведется
 */

fun main() {
    alphaOrder()
}

fun wildberriesStructuredConcurrency() {
    val coroutineContext = Job() + Dispatchers.Default
    val coroutineScope = CoroutineScope(coroutineContext)

    coroutineScope.launch {
        val request = launch {
            GlobalScope.launch {
                delay(timeMillis = 1000)
                println("1")
            }

            launch {
                delay(timeMillis = 100)
                println("2")
                delay(timeMillis = 1000)
                println("3")
            }
        }

        delay(500)
        request.cancel()
        delay(1000)
        println("4")
    } // 2 1 4
}

fun alphaOrder() {
    runBlocking {
        launch {
            delay(200L) // 2
            println("Task from runBlocking")
        }

        coroutineScope {
            launch {
                delay(500L) //  3
                println("Task from nested launch")
            }

            delay(100L) // 1
            println("Task from coroutine scope")
        }

        println("Coroutine scope is over") // 4 тк runBlocking ждет своего дитя сначала
    }
}

fun wildberriesStructuredConcurrency2() {
    var myInnerJob: Job? = null
    var myOuterJob: Job? = null
    val scope = CoroutineScope(Dispatchers.Default)

    runBlocking {
        scope.launch {
            myOuterJob = launch(Dispatchers.IO) {
                myInnerJob = launch(Job()) { // отвязана от отца
                    while (true) {
                        delay(1000L)
                    }
                }
                while (true) {
                    delay(1000L)
                }
            }
        }

        delay(2000L)
        scope.cancel()
        delay(1000L)
        println(myOuterJob?.isActive) // false
        println(myInnerJob?.isActive) // true
    }
}