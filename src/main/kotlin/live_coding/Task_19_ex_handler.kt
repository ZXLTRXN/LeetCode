package live_coding

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Какой будет вывод?
fun main() {
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