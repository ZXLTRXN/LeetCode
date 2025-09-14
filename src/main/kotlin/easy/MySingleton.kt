package easy

@Suppress("unused")
class MySingleton private constructor() {

    companion object {
        @Volatile // чтобы все точно видели изменение,
        // если без volatile то обычное взятие инстанца тоже придется в synchronized вносить
        var instance: MySingleton? = null

        val monitor: Any = Any()

        fun getInstance(): MySingleton {
            instance?.let { return it }

            synchronized(monitor) {
                instance?.let { return it } // double check - this pattern is thread-safe,
                // but requires the instance variable to be declared volatile (https://www.baeldung.com/java-implement-thread-safe-singleton)
                return MySingleton().also { instance = it }
            }
        }
    }

}