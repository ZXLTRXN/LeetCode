package live_coding

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch


// При разрывах соединения пользователям необходимо дать возможность
// альтернативного входа в конференцию, для этого мы будем показывать уведомление
// о возможности входа в конференцию по телефонному звонку.

// Необходимо показывать уведомление внутри приложения (не в центре уведомлений)
// пользователю через 10 секунд после отключения соединения.
// При восстановлении соединения уведомление должно скрываться.
// Если реконнект произошел быстрее 10 секунд - уведомление не должно показываться.

// К показу уведомления привязана аналитика по которой мы судим о том,
// что пользователю показалось уведомление на экране.

interface ConnectionManager {
    val isSocketConnected: MutableStateFlow<Boolean>
}

interface Analytics {
    fun notificationHasBeenShown()
}
// исходник
//class ConnectionNotifier(
//    private val connectionManager: ConnectionManager,
//    private val analytics: Analytics,
//    private val scope: CoroutineScope
//) {
//
//    private fun showNotification() {
//        // Реализация показа уведомления внутри приложения
//        // Например, можно использовать LiveData или StateFlow для обновления UI
//        println("Уведомление показано")
//    }
//
//    private fun hideNotification() {
//        // Реализация скрытия уведомления
//        println("Уведомление скрыто")
//    }
//}

class ConnectionNotifier(
    private val connectionManager: ConnectionManager,
    private val analytics: Analytics,
    private val scope: CoroutineScope
) {

    private var showNotificationJob: Job? = null

    fun observeConnection2(timeoutMsec: Long) {
        connectionManager
            .isSocketConnected
            .transformLatest { isConnected ->
                if (isConnected) {
                    emit(true)
                } else {
                    delay(timeoutMsec)
                    emit(false)
                }
            }
            .onEach { needToShowNotification ->
                if (needToShowNotification) {
                    showNotification()
                    analytics.notificationHasBeenShown()
                } else {
                    hideNotification()
                }
            }
            .launchIn(scope)
    }

    fun observeConnection(timeoutMsec: Long) {

        connectionManager
            .isSocketConnected
            .onEach { isConnected ->
                if (isConnected) {
                    showNotificationJob?.cancel()
                    showNotificationJob = null
                    hideNotification()
                } else {
                    showNotificationJob = scope.launch {
                        delay(timeoutMsec)
                        showNotification()
                        analytics.notificationHasBeenShown()
                    }
                    // без корутины не верно
//                    delay(timeoutMsec) // suspend весь onEach пока не исполнится
//                    showNotification()
//                    analytics.notificationHasBeenShown()
                }
            }
            .launchIn(scope)
    }

    private fun showNotification() {
        // Реализация показа уведомления внутри приложения
        // Например, можно использовать LiveData или StateFlow для обновления UI
        println("Уведомление показано")
    }

    private fun hideNotification() {
        // Реализация скрытия уведомления
        println("Уведомление скрыто")
    }
}