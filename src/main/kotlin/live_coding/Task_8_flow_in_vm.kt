package live_coding

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

/**
 * https://github.com/kish-dev/coroutines-interview?tab=readme-ov-file#solution-5
 * Сделайте рефакторинг кода,
 * а также допишите реализацию вьюмодели для получения списка моделей по запросу.
 * Пользователь вводит текст запроса, приложение отправляет запрос
 * и отдает для экрана observable screen state.
 */

data class Model(val id: Long)

interface Interactor {
    suspend fun getModels(query: String): List<Model>
}


class ScreenViewModel(
    private val interactor: Interactor,
    private val defaultDispatcherProvider: suspend () -> CoroutineContext = { Dispatchers.Default }
) {

    private val viewModelScope = CoroutineScope(SupervisorJob())

    private val _userInput = MutableStateFlow<String>("")

    private val debounceMs = 500L

    private val state: StateFlow<ScreenState> = _userInput // 1 источник стейта
//        .distinctUntilChanged() оно уже встроено в StateFlow так то
        .validate() // по приколу написал
        .transformLatest { (query, isValid) ->
            emit(ScreenState.Typing(query)) // дает мгновенный отклик на ввод, чтобы например подсказки показать
            if (query.isBlank()) {
                emit(ScreenState.Idle)
                return@transformLatest
            }
            delay(debounceMs)

            emit(ScreenState.Loading(query))
            val resultState = getModelsState(query) // withContext можно
            emit(resultState)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ScreenState.Idle
        )


    // просто интересно как реализовать
    private fun Flow<String>.validate(): Flow<Pair<String, Boolean>> {
        val pattern = Regex("[A-Za-z0-9]+")

        return flow {
            collect {
                 val isValid = withContext(defaultDispatcherProvider()) {
                    pattern.matches(it)
                }
                emit(it to isValid)
            }
        }
    }

    fun onTextChanged(value: String) {
        _userInput.value = value
    }

    private suspend fun getModelsState(query: String): ScreenState {
        return try {
            val models = interactor.getModels(query)
            if (models.isEmpty()) {
                ScreenState.Empty(query)
            } else {
                ScreenState.Loaded(query, models)
            }
        } catch (ex: Exception) {
            ScreenState.Error(query,"error")
        }
    }

    fun onCleared() {
        viewModelScope.cancel()
    }


    sealed interface ScreenState {
        data object Idle: ScreenState // инициализационный стейт - хорошая практика
        data class Typing(val query: String): ScreenState
        data class Loading(val query: String): ScreenState
        data class Empty(val query: String): ScreenState
        data class Loaded(val query: String, val models: List<Model>): ScreenState
        data class Error(val query: String, val message: String): ScreenState
    }

}