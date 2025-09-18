package live_coding

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

interface BackendApi {
    suspend fun apiCall(): String // very long call
}

// Сделать код-ревью и рефакторинг по необходимостиc
// Исходник
//class Repository(private val backendApi: BackendApi) {
//    private var cache: String? = null
//
//    suspend fun apiCallOrCache(): String {
//        if (cache != null) {
//            return cache!!
//        }
//
//        val apiResponse = backendApi.apiCall() // very long call
//        cache = apiResponse
//        return apiResponse
//    }
//}


// написал всего по максимуму
class Repository(
    private val backendApi: BackendApi,
    private val dispatcherProvider: CoroutineDispatcher = Dispatchers.IO
) {
    @Volatile
    private var cache: String? = null

    val mutex = Mutex()

    suspend fun apiCallOrCache(): Result<String> = withContext(dispatcherProvider) {
        cache?.let {
            return@withContext Result.success(it)
        }

        return@withContext mutex.withLock {
            cache?.let {
                Result.success(it)
            }
            try {
                val res = backendApi.apiCall()
                Result.success(res).also { cache = res }
            } catch (ex: CancellationException) {
                throw ex
            } catch (ex: Exception) { // если ловим Exc, то Cancellation throw нужен
                return@withContext Result.failure(ex)
            }
        }
    }
}

open class MyException(override val message: String, override val cause: Throwable) : RuntimeException(message, cause)

class MyException1(override val message: String, override val cause: Throwable) : MyException(message, cause)