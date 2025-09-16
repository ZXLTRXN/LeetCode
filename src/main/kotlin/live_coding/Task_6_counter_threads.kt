package live_coding

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

/**
 * Классический каунтер
 * проверяется многократно(в списке), чтобы точно было видно проблему
 */

fun main() {
    val resultList = mutableListOf<Int>()
//    initialTask(resultList)
//        atomicResolution(resultList)
//    syncResolution(resultList)
    lockResolution(resultList)

    // Все должны быть 0
    val res = resultList.count { it != 0 }
    println(if (res == 0)"success" else "failure $res not equal to 0")
}

fun initialTask(resultList: MutableList<Int>) {
    for (i in 0..100) {
        var counter = 0

        val one = thread(start = true) {
            for (k in 0..100000) { counter-- }
        }

        val two = thread(start = true) {
            for (j in 0..100000) { counter++ }
        }

        one.join()
        two.join()
        resultList.add(counter)
    }
}

// быстрее блокировок
fun atomicResolution(resultList: MutableList<Int>) {
    for (i in 0..100) {
        val counter = AtomicInteger(0)

        val one = thread(start = true) {
            for (k in 0..100000) { counter.decrementAndGet() }
        }

        val two = thread(start = true) {
            for (j in 0..100000) { counter.incrementAndGet() }
        }

        one.join()
        two.join()
        resultList.add(counter.get())
    }
}

fun syncResolution(resultList: MutableList<Int>) {
    val monitor = Any()
    for (i in 0..100) {
        var counter = 0

        val one = thread(start = true) {
            for (k in 0..100000) {
                synchronized(monitor) {
                    counter--
                }
            }
        }

        val two = thread(start = true) {
            for (j in 0..100000) {
                synchronized(monitor) {
                    counter++
                }
            }
        }

        one.join()
        two.join()
        resultList.add(counter)
    }
}

fun lockResolution(resultList: MutableList<Int>) {
    val lock = ReentrantLock()
    for (i in 0..100) {
        var counter = 0

        val one = thread(start = true) {
            for (k in 0..100000) { // можно лочить батчами(пачками) сразу весь цикл - оптимальней
                lock.lock()
                try { // тут конкретно лишнее, но в целом так делают
                    counter--
                } catch (ex: Exception) {
                } finally {
                    lock.unlock()
                }
            }
        }

        val two = thread(start = true) {
            for (j in 0..100000) {
                lock.lock()
                try { // тут конкретно лишнее, но в целом так делают
                    counter++
                } catch (ex: Exception) {
                } finally {
                    lock.unlock()
                }
            }
        }

        one.join()
        two.join()
        resultList.add(counter)
    }
}