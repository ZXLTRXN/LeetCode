package live_coding

import java.lang.ref.WeakReference

/**
 * видео: Проводим собеседование в Android-команду Тинькофф в прямом эфире
 * исходники
 * надо отрефакторить, изменть можно все
 */

object Const {
    val INDEX = 1
}

open class ChatSessionController(
    outerContext: Context,
    private val accountRepository: ChatAccountRepository,
    private val preferencesManager: ChatPreferencesManager,
) {

    private val monitor = Any()

    // в целом он тут не нужен, не тот слой. idPrefix можно константой
    var context: WeakReference<Context> = WeakReference(outerContext)

    companion object {
        @Volatile
        private var ownerUser: User? = null
    }

    fun initChat(): Boolean {
        if (ownerUser != null) {
            return false
        }
        val idPrefix: String = context.get()?.getString(1) ?: return false

        val accounts: List<Account> = accountRepository.getAccounts()
        var success = false

        for (i in Const.INDEX..<accounts.size) {
            val account = accounts[i]
            if (account.isOwner) {
                synchronized(monitor) {
                    if (ownerUser != null) {
                        return false
                    }
                    ownerUser = User(
                        id = idPrefix + account.id,
                        name = null,
                        phone = null,
                    )
                }
                success = true
                preferencesManager.initPreferences()
                break
            }
        }

        println("Init Chat, User = ${ownerUser}")
        return success
    }

    fun logout() {
        println("Logout Chat")
        synchronized(monitor) {
            ownerUser = null
        }
        preferencesManager.getPreferences().edit().clear().apply() // full clean
    }
}

data class User( // data
    val id: String, // val
    val name: String? = null,
    val phone: String? = null,
)

interface ChatAccountRepository {
    fun getAccounts(): List<Account>
}

interface ChatPreferencesManager {
    fun initPreferences()
    fun getPreferences(): SharedPreferences
}

// заглушки
interface SharedPreferences {
    fun edit(): SharedPreferences
    fun clear(): SharedPreferences
    fun apply(): SharedPreferences
}
class Single<T>()
class Account(val isOwner: Boolean, val id: Int)
interface Context {
    fun getString(v: Int): String
}