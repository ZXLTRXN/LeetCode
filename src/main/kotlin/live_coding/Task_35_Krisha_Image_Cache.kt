package live_coding

/**
 * Нужен кеш на основе LinkedHashMap, который будет вытеснять наиболее старые картинки, при нехватке места
 */
class ImageCache(val maxSize: Int) {

    private val cache = LinkedHashMap<String, Bitmap>(16, 0.75f, true) // в начало встают те, что реже использовались
    var currentSize: Int = 0
        private set // чтобы не считать каждый раз в цикле

    fun get(key: String): Bitmap? {
        return cache[key]
    }

    fun add(key: String, image: Bitmap) {
        val newSize = image.getSize()
        if (newSize > maxSize) return

        val prevImage = cache.remove(key)
        prevImage?.let {
            currentSize -= it.getSize()
        }

        val iterator = cache.entries.iterator()
        while (currentSize + newSize > maxSize && iterator.hasNext()) {
            val lruElement = iterator.next()
            iterator.remove()
            currentSize -= lruElement.value.getSize()
        }

        cache[key] = image
        currentSize += newSize
    }

    fun clearAll() {
        cache.clear()
        currentSize = 0
    }
}

interface Bitmap {
    fun getSize(): Int
}