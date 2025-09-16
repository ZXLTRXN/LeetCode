package live_coding

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import java.util.PriorityQueue
import java.util.Queue
import kotlin.random.Random
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


open class Person(open val age: Int) {
    companion object {
        val comparator = object: Comparator<Person> { // вперед будут съедены молодые
            override fun compare(o1: Person?, o2: Person?): Int { // null - особенности интеропа,
                // null просто меньше любого значения реализуй
                if (o1 == null && o2 == null) return 0
                if (o1 == null) return -1
                if (o2 == null) return 1

                if (o1 == o2) return 0
                return if(o1.age > o2.age) 1 else -1
            }
        }
    }

    override fun toString(): String {
        return "Person with age $age"
    }
}

class Teenager(override val age: Int): Person(age) {
    override fun toString(): String {
        return "Teenager with age $age"
    }
}

val generatePerson: () -> Person = {
    val age = Random.nextInt(101)
    if (age < 17) Teenager(age) else Person(age)
}

class WaitNotifyExample() {
    private val compLow: Comparator<in Teenager> = Person.comparator // просто для повторения написал

    private val queue: Queue<Person> = PriorityQueue(5, Person.comparator)
    private val capacity = 3
    private val monitor = Object() // wait notify у него есть, а у котлин классов нет
    private val delay: Long = 12000

    fun consumeTh() {
        while (true) {
            synchronized(monitor) {
                while (queue.isEmpty()) {
                    println("waiting people to eat")
                    monitor.wait()
                }
                while (queue.isNotEmpty()) {
                    println("eating ${queue.poll()}")
                }
                monitor.notifyAll()
            }
            Thread.sleep(delay)
        }
    }

    fun produceTh() {
        while (true) {
            synchronized(monitor) {
                while (queue.size >= capacity) {
                    println("waiting to produce people")
                    monitor.wait()
                }
                while (queue.size < capacity) {
                    val person = generatePerson.invoke()
                    println("sending person $person")
                    queue.add(person)
                }
                monitor.notifyAll()
            }
            Thread.sleep(delay)
        }
    }
}

fun CoroutineScope.produceC(capacity: Int): ReceiveChannel<Person> = produce(Dispatchers.IO, capacity) {
    while (isActive) {
        val person = generatePerson.invoke()
        println("person prepared $person")
        send(person)
        println("person sent $person") // почему sent больше чем вмещает буфер я хз, где-то складируются как будто

    }
}

fun CoroutineScope.consumeC(): SendChannel<Person> = actor {
    for (p in channel) {
        delay(3000)
        println("actor received $p")
    }
}

fun main() {

    runBlocking {

        val producer = produceC(1)
        val consumer = consumeC()

        launch {
            for(p in producer) {
                consumer.send(p)
            }
        }

        delay(3000)
        producer.cancel()   // остановим продюсера
        consumer.close()    // закроем консюмера
    }

//   val ex = WaitNotifyExample()
//    val producer = Thread { ex.produceTh() }
//    val consumer = Thread { ex.consumeTh() }
//
//    producer.start()
//    consumer.start()

}