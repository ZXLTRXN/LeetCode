package live_coding

import java.util.LinkedList

/**
 * видео: Проводим собеседование в Android-команду Тинькофф в прямом эфире
 * исходники
 * надо отрефакторить, изменть можно все
 */

//object Const {
//    val INDEX = 1
//}
//
//open class ChatSessionController(
//    private val accountRepository: ChatAccountRepository,
//    private val preferencesManager: ChatPreferencesManager,
//) {
//
//    lateinit var context: Context
//
//    companion object {
//        @Volatile
//        private var user: User? = null
//    }
//
//    @Synchronized
//    fun initChat(): Single<Boolean> {
//        val success = false
//        val idPrefix = context.getString(1)
//        var range = accountRepository.getAccounts().size
//        for (i in Const.INDEX..range) {
//            val account = accountRepository.getAccounts()[i]
//            if (account.isOwner) {
//                user = User(
//                    id = idPrefix + account.id,
//                    name = null,
//                    phone = null,
//                )
//            }
//        }
//
//        if (success == true) {
//            preferencesManager.initPreferences()
//        }
//
//        println("Init Chat, User = ${user}")
//        return Single() // не хватало в исходнике
//    }
//
//    fun logout() {
//        println("Logout Chat")
//        preferencesManager.getPreferences().edit().clear().apply()
//    }
//}
//
//class User(
//    var id: String,
//    var name: String? = null,
//    var phone: String? = null,
//)
//
//interface ChatAccountRepository {
//    fun getAccounts(): LinkedList<Account>
//}
//
//interface ChatPreferencesManager {
//    fun initPreferences()
//    fun getPreferences(): SharedPreferences
//}
//
//// заглушки
//interface SharedPreferences {
//    fun edit(): SharedPreferences
//    fun clear(): SharedPreferences
//    fun apply(): SharedPreferences
//}
//class Single<T>()
//class Account(val isOwner: Boolean, val id: Int)
//interface Context {
//    fun getString(v: Int): String
//}