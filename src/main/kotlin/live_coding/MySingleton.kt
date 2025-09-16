package live_coding

@Suppress("unused")
class MySingleton private constructor() {

    companion object {
        @Volatile // чтобы все точно видели изменение,
        // если без volatile то обычное взятие инстанца тоже придется в synchronized вносить
        private var instanceValue: MySingleton? = null

        private val monitor: Any = Any()

        fun getInstance(): MySingleton {
            instanceValue?.let { return it }

            synchronized(monitor) {
                instanceValue?.let { return it } // double check - this pattern is thread-safe,
                // but requires the instance variable to be declared volatile (https://www.baeldung.com/java-implement-thread-safe-singleton)
                return MySingleton().also { instanceValue = it }
            }
        }
    }

}