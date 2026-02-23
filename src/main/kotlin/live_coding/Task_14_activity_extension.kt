package live_coding

import java.io.Serializable

// Сделать эту функцию extension для активити и исправить ошибки
//fun <T : Serializable?> getSerializable(name: String) {
//    return intent.getSerializableExtra(name) as T
//}


inline fun <reified T : Serializable> Activity2.getSerializable(
    name: String
): T? {
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return intent.getSerializableExtra(name, T::class.java)
//    } else {
//        @Suppress("DEPRECATION")
//        intent.getSerializableExtra(name) as? T
//    }
}


class Activity2() {
    val intent: Intent1 = Intent1()
}

class Intent1 {
    fun getSerializableExtra(key: String): Any? { return null }
    fun <T>getSerializableExtra(key: String, clazz: Class<T>): T? {
        return key as T
    }
}

fun main() {
    val act = Activity2()
    val user = act.getSerializable<Int>("1")
}