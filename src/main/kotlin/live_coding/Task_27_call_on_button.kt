package live_coding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Вам доступно некоторое API:
 * https://api.yandex.net/offer/{idy/phones.json
 *
 * Формат ответа:
 * {
 *     "phones": ["+7 (999)-999-00-01", "+7 (999)-999-00-02"]
 * }
 *
 * Есть репозиторий PhoneRepository, который ходит в эту ручку, и есть метод getPhones(),
 * который возвращает список номеров List<String>
 * interface PhoneRepository {
 *     getPhones(): List<string> // suspend fun getPhones(): List<String>, если решите использовать корутины
 * }
 *
 * Задание:
 * - Необходимо, чтобы по нажатию на некоторую кнопку на экране дёргалось эта ручка
 * и совершался переход в звонилку с полученным номером.
 * - Требуется написать весь UI и необходимую бизнес-логику для совершения этого действия.
 * - Иных элементов интерфейса (кроме кнопки) на экране нет.
 *
 * Примечание:
 * - Технологический стек - на ваш выбор, но важно обосновать принятое решение.
 * - Реализовывать репозиторий не нужно.
 */

// Как защититься от того что юзер нажал кнопку 20 раз подряд?

interface PhoneRepository {
    suspend fun getPhones(): List<String>
}

class ViewModel1(
    val repo: PhoneRepository
) {
    private val viewModelScope = CoroutineScope(SupervisorJob())

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state

    private val _action = Channel<Action>(Channel.BUFFERED)
    val action = _action.receiveAsFlow()

    fun onClick() {
        requestPhone()
    }

    private fun requestPhone() {
        viewModelScope.launch {
            _state.emit(State.Blocked)
            val phones = try {
                val phones = repo.getPhones()
                if (phones.isEmpty()) {
                    _action.send(Action.ShowSuccess(phones.first()))
                } else {
                    _action.send(Action.ShowEmpty("empty"))
                }

            } catch (ex: Exception) {
                _action.send(Action.ShowError("error"))
            }
            _state.emit(State.Idle)
        }
    }

    sealed interface State {
        data object Blocked: State
        data object Idle: State
    }

    sealed interface Action {
        data class ShowError(val title: String): Action
        data class ShowEmpty(val title: String): Action
        data class ShowSuccess(val number: String): Action
    }
}

class Activity1() {
    fun onCreate() {

    }

    fun callIntent(phone: String) {
//        val intent = Intent(ACTION).apply {
//            data Uri.parse(tel)
//        }
//        startActivity(intent)
    }

    fun observe(flow: Flow<String>) {
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Started) {
//                flow.collectLatest {
//
//                }
//            }
//        }

//        COMPOSE
//        val state by viewModel.uiState.collectAsStateWithLifecycle()
//        val flow by viewModel.flow.collectAsState(initial = emptyList())
//        LaunchedEffect(viewModel) { // channel = _channel.receiveAs Flow
//            viewModel.events.collect { event ->
//                when (event) {
//                    is UiEvent.ShowToast -> {
//                    }
//                    is UiEvent.Navigate -> {
//                    }
//                }
//            }
//        }

    }
}




