package live_coding

import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Моя задача Яндекс
 * напиши реализацию runRequests
 * mapNotNull и обработку ошибок добавил сам просто для полноты,
 * в оригинале можно было игнорить ошибки
 * НАПИШИ ТЕСТЫ хорошая задача потренироваться
 */
interface Response {
    val isError: Boolean
}

interface Request {
}

interface Api {
    fun request(request: Request, callback: (Response) -> Unit)
    fun cancelRequest(request: Request)
}

class Service(
    private val api: Api,
    private val dispatchersProvider: () -> CoroutineDispatcher
) {
    suspend fun runRequests(
        requests: List<Request>,
        callback: (List<Response>) -> Unit
    ) = withContext(dispatchersProvider()) {
        supervisorScope {
            val responseDeferred = requests.map {
                async { request(it) }
            }

            val results: List<Response> = responseDeferred.mapNotNull { deferred ->
                try {
                    deferred.await()
                } catch (ex: CancellationException) {
                    throw ex
                }catch (ex: Exception) {
                    null
                }
            }

            callback(results)
        }
    }

    /**
     * @throws RuntimeException
     */
    suspend fun request(request: Request): Response {
        return suspendCancellableCoroutine { continuation ->

            api.request(request) { response ->
                if (continuation.isActive) {
                    if (response.isError) {
                        continuation.resumeWithException(RuntimeException("example"))
                    } else {
                        continuation.resume(response)
                    }

                }
            }

            continuation.invokeOnCancellation {
                api.cancelRequest(request)
            }
        }
    }
}

fun main() {

}


data class TestRequest(val id: Int) : Request

data class TestResponse(
    override val isError: Boolean,
    val data: String
) : Response

