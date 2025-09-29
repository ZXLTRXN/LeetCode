package live_coding

import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier


fun main() {
    yandexTask()
}

/**
 * 1. Надо добиться чтобы было выведено 1 1, то есть потоки ждали друг друга. Смотри [condition]
  */
private class State {
    @Volatile // чтобы потоки изменения видели
    var x: Int = 0

    @Volatile // чтобы потоки изменения видели
    var y: Int = 0
}

private fun condition() {
    val state = State()

    val t1 = Thread {
        state.x = 1
        println(state.y)
    }

    val t2 = Thread {
        state.y = 1
        println(state.x)
    }

    t1.start()
    t2.start()
    t1.join()
    t2.join()
}

private fun countDownLatchSolution() {
    val state = State()
    val latch1 = CountDownLatch(1)
    val latch2 = CountDownLatch(1)

    val t1 = Thread {
        state.x = 1
        latch1.countDown()
        latch2.await()
        println(state.y)
    }

    val t2 = Thread {
        state.y = 1
        latch2.countDown()
        latch1.await()
        println(state.x)
    }

    t1.start()
    t2.start()
    t1.join()
    t2.join()
}

private fun barrierSolution() {
    val state = State()
    val barrier = CyclicBarrier(2)

    val t1 = Thread {
        state.x = 1
        barrier.await()
        println(state.y)
    }

    val t2 = Thread {
        state.y = 1
        barrier.await()
        println(state.x)
    }

    t1.start()
    t2.start()
    t1.join()
    t2.join()
}

private class Incrementer {
    var count = 0

    @Synchronized
    fun inc() {
        if (count < 100000) {
            count++
            inc()
        }
    }
}

/**
 * Оно и так всегда норм работает, хз какая была задача
 */
private fun yandexTask() {
    val incrementer = Incrementer()
    val threads = List(200) {
        Thread {
            incrementer.inc()
        }
    }

    threads.forEach {
        it.start()
    }

    threads.forEach { it.join() }
    println(incrementer.count)

}

