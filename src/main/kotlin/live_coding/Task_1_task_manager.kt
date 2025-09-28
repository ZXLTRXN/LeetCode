@file:Suppress("unused")
package live_coding

import kotlin.random.Random

/*
 * Необходимо реализовать класс TaskManager, который представляет собой планировщик задач.
 * Task и тела методов TaskManager писал сам
 */

data class TaskM(
    val action: () -> Unit,
    val delay: Long,
    val startDate: Long = System.currentTimeMillis()
) {
    val executionTime: Long = startDate + delay
}



class TaskManager {

    // коллекцию написал от балды, может есть и оптимальней
    // либо можно на потокобезопасных коллекциях без synchronized обойтись
    val tasks: MutableList<TaskM> = mutableListOf()

    private val lock = Any()

    /*
     *  Метод addTask, который принимает исполняемый код задачи и опциональную задержку перед ее выполнением (в миллисекундах).
     *  Метод должен возвращать объект типа Task, который представляет добавленную задачу.
     */
    fun addTask(action: () -> Unit, delay: Long = 0L): TaskM {
        synchronized(lock) {
            val task = TaskM(action, delay)
            tasks.add(task)
            return task
        }
    }

    /*
     * Метод act периодически вызывается извне и выполняет активные задачи в соответствии с текущим временем.
     */
    fun act() {

        val currentTime = System.currentTimeMillis() // было в задании

        val tasksForExecution = synchronized(lock) {
            val tasksForDelete = tasks.filter {
                it.executionTime <= currentTime
            }
            tasks.removeAll(tasksForDelete)

            tasksForDelete
        }

        tasksForExecution.forEach {
            it.action.invoke()
        }
    }

}

// Пример добавления задачи
fun main() {

    val taskManager = TaskManager()

    // Пример того как вызывается метод act
    Thread {
        while (true) {
            // Выполнение невыполненных задач
            taskManager.act()
            // Случайная задержка
            Thread.sleep(Random.nextLong(5000))
        }
    }.start()

    val task1 = taskManager.addTask({
        println("first task")
    }, 2000)

    val task2 = taskManager.addTask({
        println("second task")
    })

    val task3 = taskManager.addTask({
        println("third task")
    }, 3000)
    /*
    * Код выше должен вывести строки в очередности:
    * second task
    * first task
    * third task
    */
}
