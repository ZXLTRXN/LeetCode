package live_coding

import kotlinx.coroutines.*

interface DocumentsApi {
    // returns list of ids
    suspend fun getAllIds(): List<String>
    // returns content for id
    suspend fun getContent(id: String): String
}

data class Document(val id: String, val content: String)

interface DocumentsFetcher {
    suspend fun getDocuments(): List<Document>
}

// To implement:
class DocumentsFetcherImpl(
    private val api: DocumentsApi,
    private val ioDispatcherProvider: CoroutineDispatcher
): DocumentsFetcher {
    override suspend fun getDocuments(): List<Document> = coroutineScope {
        val ids = api.getAllIds()

        return@coroutineScope ids.map { id ->
            async {
                 getDocumentOrNull(id)
            }
        }.awaitAll()
            .filterNotNull()
    }

    private suspend fun getDocumentOrNull(id: String): Document? = withContext(ioDispatcherProvider) {
        return@withContext try {
            val content: String = api.getContent(id)
            Document(id, content)
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Exception) {
            println(ex)
            null
        }
    }
}