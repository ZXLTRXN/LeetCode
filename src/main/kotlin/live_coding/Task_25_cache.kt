package live_coding

/**
 * Найти ошибки
 */
abstract class StringCache
protected constructor(private val keys: MutableSet<ByteArray>) { // коллекция как ключ - может меняться,
    // кроме того у массива байтов не переопределен equals и hashCode вообще,
    // то есть не будет нормально работать

    private var map: MutableMap<ByteArray, String> =
        //var зачем?, и на вар лочится тоже херь полная,
        // будет объект синхронизации меняться постоянно
        HashMap<ByteArray, String>()

    fun get(key: ByteArray): String? {
        if (!map.isEmpty()) { // double check (проверка в synchronized + volatile)
            return map[key]
        }
        synchronized(map) {
            fillCache()
        }
        return map[key]
    }

    private fun fillCache() {
        map = HashMap<ByteArray, String>()
        for (key in keys) {
            map[key] = calculateStr(key)
        }
    }

    protected abstract fun calculateStr(key: ByteArray): String
}

class A(val keys: MutableSet<ByteArray>): StringCache(keys = keys) {
    override fun calculateStr(key: ByteArray): String {
        TODO("Not yet implemented")
    }
}