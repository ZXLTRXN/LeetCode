package live_coding

import com.apple.eawt.Application
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.collections.onEach
import kotlin.collections.toList
import kotlin.sequences.toList
import kotlin.text.onEach
import kotlin.text.toList
import kotlin.toList

//Исходник
//class ExoPlayerController(
//    val playerHolder: PlayerHolder,
//    val playerAnalytics: PlayerAnalytics,
//    var context: Context,
//) {
//    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
//
//    var currentInfo: ActivePlayersModel = ActivePlayersModel(
//        countActivetedPlayers = 0,
//        allPlayersIds = emptyList(),
//    )
//
//    fun initPlayer(model: AudioModel) {
//        scope.launch {
//            try {
//                init(model)
//            } catch (e: Exception) {
//                Log.e("ExoPlayerController", "init player error = ${it.message}", e)
//            }
//        }
//    }
//
//
//    fun init(model: AudioModel) {
//        playerHolder.getPlayer(model.id)
//            .onEach { player ->
//                val newPlayer = if (player == null) {
//                    ExoPlayer.Builder(context).build()
//                } else {
//                    player
//                }
//                playerHolder.addPlayer(model.id, newPlayer)
//                player
//            }
//            .flowOn(Dispatchers.IO)
//            .flatMapConcat { player ->
//                Single.fromCallable {
//                    val isNeedPrepare =
//                        model.fileSize <= (1024 * 1024) || player.currentPosition > 0
//                    if (isNeedPrepare) {
//                        player.prepare()
//                    }
//                }
//            }
//            .flowOn(Dispatchers.Unconfined)
//            .onEach {
//                var newInfo = ActivePlayersModel(
//                    countActivetedPlayers = currentInfo.countActivatedPlayers + 1,
//                    allPlayersIds = currentInfo.allPlayersIds.plus(model.id),
//                )
//                newInfo
//            }
//            .collect { newInfo -> currentInfo = newInfo }
//    }
//
//
//    @Synchronized
//    fun stopPlayers() {
//        playerHolder.getPlayers()
//            .flowOn(Dispatchers.Unconfined)
//            .map {
//                it.asFlow()
//            }
//            .filter { player ->
//                player.isPlaying
//            }
//            .onEach { player ->
//                player?.stop()
//            }
//            .toList()
//            .apply {
//                playerAnalytics.sendPlayersAnalytics(
//                    currentInfo.countActivatedPlayers,
//                    currentInfo.allPlayersIds
//                )
//                currentInfo = ActivePlayersModel(
//                    countActivetedPlayers = 0,
//                    allPlayersIds = emptyList(),
//                )
//            }
//    }
//}
//
//class ActivePlayersModel(
//    val countActivatedPlayers: Int,         // количесвто активированных players
//    val allPlayersIds: List<String>,        // id всех players которые пытались активировать
//)
//
//class AudioModel(
//    val id: String,
//    val audioUrl: String,
//    val fileSize: Long,
//    val title: String?,
//    val duration: Long,
//)
//
//interface PlayerHolder {
//
//    /**
//     * Данный holder по условию задачи может хранить десятки тысяч players
//     */
//
//    fun addPlayer(playerId: String, player: ExoPlayer): Flow<Unit>
//
//    fun getPlayer(playerId: String): Flow<ExoPlayer?>
//
//    fun getPlayers(): Flow<List<ExoPlayer?>>
//}
//
//interface PlayerAnalytics {
//
//    fun sendPlayersAnalytics(countActivatedPlayers: Int, allPlayersIds: List<String>)
//}

// не стесняйся уходить от flow, они тут не нужны
// flatMapConcat { list -> list.asFlow() } flow<List> -> flow<Element>

class ExoPlayerController(
    private val playerHolder: PlayerHolder,
    private val playerAnalytics: PlayerAnalytics,
    private val context: Application, // и сервис и активита хранят ссылку на applicationContext
) {
    //    val ref = WeakReference<Context>(context) // .get = Context? просто напоминание про такой вариант
    private val mutex = Mutex()

    @Volatile
    var currentInfo: ActivePlayersModel = ActivePlayersModel(
        allPlayersIds = emptyList(),
    )
        private set

    suspend fun initPlayer(model: AudioModel) {
        withContext(Dispatchers.Main) { // плееры инициализируются на мейне
            try {
                init(model)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
//                Log.e("ExoPlayerController", "init player error = ${it.message}", e)
            }
        }
    }

    private suspend fun init(model: AudioModel) {
        val player = playerHolder.getPlayer(model.id).firstOrNull() ?: ExoPlayer.Builder(context).build()
        playerHolder.addPlayer(model.id, player).firstOrNull()
        val isNeedPrepare = model.fileSize <= (1024 * 1024) || player.currentPosition > 0
        if (isNeedPrepare) {
            player.prepare()
        }

        mutex.withLock {
            currentInfo = ActivePlayersModel(
                allPlayersIds = currentInfo.allPlayersIds.plus(model.id),
            )
        }
    }

    // можно было тоже полностью уйти от флоу
    suspend fun stopPlayers() {
        playerHolder.getPlayers()
            .onStart {
                mutex.withLock {
                    playerAnalytics.sendPlayersAnalytics(
                        currentInfo.countActivatedPlayers,
                        currentInfo.allPlayersIds
                    )
                }
            }
            .first()
            .asFlow()
            .filterNotNull()
            .flowOn(Dispatchers.IO)
            .filter { player ->
                player.isPlaying
            }.onEach { player ->
                player.stop()
            }
            .flowOn(Dispatchers.Main)
            .onCompletion { th ->
                mutex.withLock {
                    currentInfo = ActivePlayersModel(
                        allPlayersIds = emptyList(),
                    )
                }
            }
            .collect {  }
    }
}

class ActivePlayersModel(
    val allPlayersIds: List<String>,        // id всех players которые пытались активировать
) {
    val countActivatedPlayers: Int
        get() { // количесвто активированных players
            return allPlayersIds.size
        }
}

class AudioModel(
    val id: String,
    val audioUrl: String,
    val fileSize: Long,
    val title: String?,
    val duration: Long,
)

interface PlayerHolder {

    /**
     * Данный holder по условию задачи может хранить десятки тысяч players
     */

    fun addPlayer(playerId: String, player: ExoPlayer): Flow<Unit>

    fun getPlayer(playerId: String): Flow<ExoPlayer?>

    fun getPlayers(): Flow<List<ExoPlayer?>>
}

interface PlayerAnalytics {

    fun sendPlayersAnalytics(countActivatedPlayers: Int, allPlayersIds: List<String>)
}

// недостающее
interface ExoPlayer {
    val currentPosition: Int
    val isPlaying: Boolean

    fun prepare()
    fun stop()

    class Builder(context: Application) {
        fun build(): ExoPlayer {
            return object : ExoPlayer {
                override val currentPosition: Int
                    get() = 1

                override val isPlaying: Boolean
                    get() = true

                override fun prepare() {
                }

                override fun stop() {
                }
            }
        }
    }
}
