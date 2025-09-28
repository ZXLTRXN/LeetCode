package live_coding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Какой будет вывод и почему?
 * Как сделать так, чтобы вывелось все?
  */


fun main() {
    runBlocking {
        val someScope = CoroutineScope(Job())
        val flow = MutableSharedFlow<Int>()
        val channel: Channel<Int> = Channel(capacity = 10) // решение

        someScope.launch {
            flow.emit(1)
            channel.send(1)
            flow.emit(2)
            channel.send(2)
            flow.emit(3)
            channel.send(3)
            delay(200)
            flow.emit(4)
            channel.send(4)
        }

        someScope.launch {
            delay(100)
            flow.collectLatest {
                println(it)
            }
        }

        someScope.launch {
            delay(100)
            channel.receiveAsFlow().collectLatest {
                println("ch $it")
            }
        }
        delay(1000)
        someScope.cancel()
    } // 4, тк все что до коллекта при 0 реплае отбрасывается

}