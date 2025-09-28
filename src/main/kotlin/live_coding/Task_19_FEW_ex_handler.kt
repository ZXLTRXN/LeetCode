package live_coding

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Какой будет вывод?
fun main() {
    x5unHandled()
}

fun alphaHandler() {
    runBlocking {
        val handler = CoroutineExceptionHandler { _, _ -> println("1") }
        val handler2 = CoroutineExceptionHandler { _, _ -> println("2") }
        val handler3 = CoroutineExceptionHandler { _, _ -> println("3") }

        val scope = CoroutineScope(handler)

        scope.launch(handler2) {
            launch(handler3) {
                launch() {
                    try {
                        val workDef = async { throw RuntimeException() }
                        workDef.await()
                    } catch (e: Exception) {
                        println("Exception caught in inner launch")
                    }
                }
            }
        }
        delay(1000)
    }
}

fun x5Handler() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
    }

    val topLevelScope = CoroutineScope(Job())

    topLevelScope.launch { // default
        println("1" + Thread.currentThread().name)
        launch(coroutineExceptionHandler) { // на уровень выше, чтобы поймать // default другой
            println("2" + Thread.currentThread().name)
            throw RuntimeException("RuntimeException in nested coroutine")
        }
    }
    println("3" + Thread.currentThread().name) // main
    Thread.sleep(100) // чтобы ошибка успела показаться
    // print с тредом мои
}

fun x5unHandled() {
    val topLevelScope = CoroutineScope(Job())
    topLevelScope.launch {
        try {
            launch {
                throw RuntimeException("RuntimeException in nested coroutine")
            }
        } catch (exception: Exception) {
            println("Handle $exception")
        }
    }
    Thread.sleep(100)
}