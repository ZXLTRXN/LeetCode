package live_coding

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Написать таймер для TextView, который будет менять значение каждую секунду от пяти до единицы.
 * Когда дойдём до нуля нужно открыть следующую активность
 *
 * ВАЖНО:
 * нужно было еще спросить важно ли значения хранить, тогда бы делал через StateFlow,
 * даже на такой казалось бы простой задаче, нужно не пропускать этап вопросов и проектирования
 * чел в видео сделал с помощью stateFlow (вывод в TextView)
 * и SharedFlow (для того чтобы запускать следующее активити)
 *
 * Когда делаешь что-то зависимое от андроида, то обращай внимание на архитектуру.
 * Например, здесь выполнил задачу в лоб и написал таймер.
 * Таймер верный, однако я не подумал что писать его в активити это неверно
 * я должен был эту логику перенести во вьюмодель!
 */
class Activity(parentJob: Job?) {

    val textView = TextView()

    val lifecycleScope = CoroutineScope(SupervisorJob(parentJob))
    val vm = ViewModel()

    fun onCreate(savedInstanceState: Any?) {
        if (savedInstanceState == null) { // чтобы при повороте не перезапускать
            vm.countDownFlow(5)
                .onEach {
                    textView.setText(it.toString())
                }
                .onCompletion { th ->
                    if (th == null) {
                        launchSecondActivity()
                    } else {
                        println("completed with $th")
                    }
                }
                .launchIn(lifecycleScope)
        }

    }

    fun onDestroy() {
        lifecycleScope.cancel("onDestroy")
    }

    private fun launchSecondActivity() {
        println("launchSecondActivity")
    }
}

class TextView {
    fun setText(text: String) {
        println("setText $text")
    }
}

class ViewModel() {

    /**
     * описать поведение таймера
     */
    fun countDownFlow(
        durationSec: Int,
    ): Flow<Int> = flow {
        var countDown: Int = durationSec
        // проверка на currentCoroutineContext().isActive не нужна,
        // она в suspend функциях сама выполнится
        while (countDown > 0) {
            emit(countDown)
            countDown -= 1
            delay(1_000L)
        }
    }

}

fun main() {
    runBlocking {
        val activity = Activity(coroutineContext[Job])
        activity.onCreate(null)
        delay(6000)
        activity.onDestroy()
    }

}