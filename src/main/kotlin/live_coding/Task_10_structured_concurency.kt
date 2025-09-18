package live_coding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Что выведется
 */
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