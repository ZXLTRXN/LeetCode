package live_coding

import java.util.LinkedList

class SimpleHandler : Thread() {
    // private
    internal var alive = true //volatile atomicBoolean излишен, тк нет прочитал, а потом записал(используя if)
    internal val taskQueue = LinkedList<Runnable>() // concurrent collection

// init{start()} не хватает

    /**
    This method posts the task into the thread
     */
    fun post(task: Runnable) {
        taskQueue.add(task)
    }

    /**
    Stops the handler
     */
    fun quit() {
        alive = false
    }

    override fun run() {
        while (alive) {
            val task = taskQueue.get(0) // poll + null check
            task.run()
        }
    }
}