package live_coding

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Сделайте рефакторинг кода,
 * а также допишите реализацию вьюмодели для получения списка моделей по запросу.
 * Пользователь вводит текст запроса, приложение отправляет запрос
 * и отдает для экрана observable screen state.
 */

class Model()

interface Interactor {

    suspend fun getModels(query: String): List<Model>
}

class ScreenViewModel(private val interactor: Interactor) {

    val list: MutableStateFlow<List<Model>> = MutableStateFlow(emptyList())

    fun onTextChanged(value: String) {
        //todo realization
    }

}