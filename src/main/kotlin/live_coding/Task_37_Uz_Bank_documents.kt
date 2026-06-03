package live_coding

import kotlinx.coroutines.*


class Task_37_Uz_Bank_documents(
    private val api: DocumentsApi
) {
    private suspend fun getDocuments(): List<String> = coroutineScope {
        val countThreads = 5
        // Создаем кастомный диспетчер с ограничением в 5 потоков
        val dispatcher = Dispatchers.IO.limitedParallelism(countThreads)

        // Получаем список ID
        val ids = api.getAllIds()
        withContext(dispatcher) {
            ids.map { id ->
                async(dispatcher,
                    start = CoroutineStart.LAZY // не обязательно, просто напомнить
                ) {
                    api.getContent(id)
                }
            }.awaitAll()
        }
    }
}
