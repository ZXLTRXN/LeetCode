@file:Suppress("unused")
package live_coding

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * https://github.com/kish-dev/coroutines-interview
 *
 * В коде кидается exception в функции withExc() хочется,
 * чтобы корутина могла обработать ошибку и не завершалась.
 */

/**
 * Сейчас в изначальном коде обработка ошибок опирается на ceh,
 * который находится в корутине one. Обработки ошибок не происходит,
 * так как one - Job, а не SupervisorJob -> обработка должна производиться в скоупе cs
 * Возможное решение 1 неверное: перенести ceh в cs (до end не дойдем)
 * Возможное решение 2 неверное: сделать one supervisorJob (сработает, но это какой-то костыль)
 * решение 3: try catch внутри launch либо вокруг скопа
 */
suspend fun sourceCodeWithProblem(ceh :CoroutineExceptionHandler) {
    val cs = CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
        val one = launch(ceh) {
            withExc()
        }
        val two = launch() {
            delay(2000)
            println("end")
        }

        one.join()
        two.join()
    }

    cs.join()
}

suspend fun solution(ceh :CoroutineExceptionHandler) {
    val cs = CoroutineScope(Dispatchers.Default + SupervisorJob() + ceh).launch {
        val one = launch {
            try {
                withExc()
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Exception) {
                println(ex)
            }
        }
        val two = launch() {
            delay(2000)
            println("end")
        }

        one.join()
        two.join()
    }

    cs.join()
}

fun main() {
    val ceh = CoroutineExceptionHandler { _, exc ->
        println("CEH: ${exc.message}")
    }

    runBlocking {
//        sourceCodeWithProblem(ceh)
        solution(ceh)
    }
}

suspend fun withExc(): String {
    delay(500)
    throw RuntimeException("withExcException")
}