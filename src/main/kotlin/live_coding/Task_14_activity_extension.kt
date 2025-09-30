package live_coding

import java.io.Serializable

// Сделать эту функцию extension для активити и исправить ошибки
//fun <T : Serializable?> getSerializable(name: String) {
//    return intent.getSerializableExtra(name) as T
//}

fun <T: Serializable> Activity2.getSerializable(key: String): T? {
    return intent.getSerializableExtra(key) as? T
}


class Activity2() {
    val intent: Intent1 = Intent1()
}

class Intent1 {
    fun getSerializableExtra(key: String) { // хз какой апи тут
    }
}

fun main() {
    val act = Activity2()
    val user = act.getSerializable<Int>("1")
}